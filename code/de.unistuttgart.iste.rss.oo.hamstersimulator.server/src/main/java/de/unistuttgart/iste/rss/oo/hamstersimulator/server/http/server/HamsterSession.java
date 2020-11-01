package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.SpeedChangedOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.Delta;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.InputMessage;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static de.unistuttgart.iste.rss.utils.Preconditions.*;

/**
 * A session represents a HamsterGame from the server's point of perspective
 * it handles all state management and communication to the client. It
 * also keeps track of its own lifetime
 */
public class HamsterSession {

    /**
     * The interval after which the session should shut itself down, if no message /
     * request occurred
     */
    private static final int MIN_SHUTDOWN_DELAY = 4000;

    /**
     * lock used for read write synchronization
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    /**
     * List with all deltas. To reconstruct the state of the client's HamsterGame,
     * all deltas should be applied
     */
    private final List<Delta> deltaList = new ArrayList<>();
    /**
     * for communication with the client
     * uses socket for the underlying communication
     */
    private final ObjectOutputStream outputStream;
    /**
     * creates an executable task out of each received operation
     */
    private final LambdaVisitor<Operation, Runnable> operationVisitor;
    /**
     * the id of this session
     * this is provided as argument in the constructor, the id
     * management is up to the server itself
     */
    private final int id;
    /**
     * the socket for the communication with the client
     */
    private final Socket socket;

    /**
     * if this is false, the session was shut down
     * the socket is closed, and no non-damon threads are running
     * it is no longer possible to communicate with the client
     */
    private volatile boolean isAlive = true;
    /**
     * the last timestamp when this session was updated
     * updates are operations from the client or updates / other requests from the
     * server
     */
    private volatile long lastKeepAliveTime = System.currentTimeMillis();

    /**
     * the current Mode of the game
     */
    private volatile Mode mode;
    /**
     * null if there is currently no input
     * because of the delay between client and server, this might be outdated
     */
    private volatile InputMessage inputMessage;
    /**
     * true if there are commands which can be undone
     */
    private volatile boolean canUndo;
    /**
     * true if there are commands which can be redone
     */
    private volatile boolean canRedo;
    /**
     * the current speed of the game, this is always between 0 and 10
     */
    private volatile double speed;

    /*@
     @ requires socket != null;
     @ requires !socket.isClosed();
     @*/
    /**
     * Creates a new HamsterSession with the provided socket, which will be used
     * for the communication with the client, and the specified id
     * @param socket socket used for communication with the client
     * @param id the id of the session, used by the server to identify sessions
     * @throws IOException if it is not possible to establish the communication with
     *                     the client
     * @throws IllegalArgumentException if the socket is closed
     */
    public HamsterSession(final Socket socket, final int id) throws IOException {
        checkNotNull(socket, "socket must be != null");
        checkArgument(!socket.isClosed(), "the socket must not be closed");

        this.id = id;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.operationVisitor = new LambdaVisitor<Operation, Runnable>()
                .on(AbortInputOperation.class).then(operation -> () -> onAbortInput(operation))
                .on(AddDeltaOperation.class).then(operation -> () -> onAddDelta(operation))
                .on(AddDeltasOperation.class).then(operation -> () -> onAddDeltas(operation))
                .on(RequestInputOperation.class).then(operation -> () -> onRequestInput(operation))
                .on(ModeChangedOperation.class).then(operation -> () -> onModeChanged(operation))
                .on(CanUndoChangedOperation.class).then(operation -> () -> onCanUndoChanged(operation))
                .on(CanRedoChangedOperation.class).then(operation -> () -> onCanRedoChanged(operation))
                .on(SpeedChangedOperation.class).then(operation -> () -> onSpeedChanged(operation));

        startInputListener(socket);
    }

    /*@
     @ requires socket != null;
     @ requires isAlive();
     @ requires !socket.isClosed();
     @*/
    /**
     * Starts to listen for operations from the client on a new thread.
     * This should only be called once per socket. If the socket is closed, the thread
     * is terminated and the shutdown initiated
     * @param socket the Socket used to receive Operations from the client
     * @throws IOException if it is not possible to establish the connection to the client
     * @throws IllegalArgumentException if the socket is closed
     * @throws IllegalStateException if this session isn't alive
     */
    private void startInputListener(final Socket socket) throws IOException {
        checkNotNull(socket, "socket must be != null");
        checkState(isAlive(), "the session must not be closed to start listening for input");
        checkArgument(!socket.isClosed(), "the socket must not be closed");

        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        final Thread thread = new Thread(() -> {
            try {
                listenForOperations(socket, inputStream);
            } catch (Exception e) {
                shutdown();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /*@
     @ requires socket != null;
     @ requires inputStream != null;
     @*/
    /**
     * Listens blocking for new operations from the client
     * @param socket the socket which is used for the inputStream, used to check if it is still open
     * @param inputStream used to receive and decode operations from the client
     * @throws IOException if the socket was closed while listening for operations
     * @throws ClassNotFoundException if the client send an operation which is unknown to the server
     */
    private void listenForOperations(final Socket socket, final ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException {
        while (!socket.isClosed()) {
            final Object input = inputStream.readObject();
            keepAlive();
            final Runnable handler = operationVisitor.apply(input);
            if (handler == null) {
                throw new IllegalStateException("no handler found for the operation");
            } else {
                handler.run();
            }
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Sends and operation to the client via the the outputStream
     * if this is not possible, the shutdown is initiated.
     * Requires that the session is alive
     * @param operation the Operation which is sent to the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    public void sendOperation(final Operation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "the session must not be closed to send an operation");

        keepAlive();
        this.readWriteLock.writeLock().lock();
        try {
            outputStream.writeObject(operation);
        } catch (IOException e) {
            shutdown();
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires true;
     @ ensures this.socket.isClosed();
     @ ensures !this.isAlive();
     @*/
    /**
     * Shuts this session down. This closes the socket and
     * therefore ensures that all non-damon threads are interrupted
     */
    private void shutdown() {
        this.readWriteLock.writeLock().lock();
        try {
            if (this.isAlive) {
                this.isAlive = false;
                try {
                    this.socket.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires true;
     @ ensures ((System.currentTimeMillis() - this.lastKeepAliveTime) > MIN_SHUTDOWN_DELAY) ==> !isAlive();
     @*/
    /**
     * Shuts this session down if it was not updated for longer than
     * MIN_SHUTDOWN_DELAY.
     * This should be called by the server to eliminate unused sessions from time to time
     */
    public void shutdownIfPossible() {
        if ((System.currentTimeMillis() - this.lastKeepAliveTime) > MIN_SHUTDOWN_DELAY) {
            shutdown();
        }
    }

    /*@
     @ requires true;
     @ ensures this.lastKeepAliveTime == System.currentTimeMillis();
     @*/
    /**
     * Updates the lastKeepAliveTime and therefore keeps this session alive.
     * While this is called regularly, this session is not shut down by shutdownIfPossible
     */
    private void keepAlive() {
        this.lastKeepAliveTime = System.currentTimeMillis();
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Returns a list with all deltas after index (inclusive)
     * Warning: the returned list is mutable!
     * This does not requires that index actually exists, it is automatically
     * clamped if necessary
     * @param index the index after which (inclusive) all further deltas should be returned
     * @return the list with all deltas after index (inclusive)
     */
    private List<Delta> getDeltasSince(final int index) {
        readWriteLock.readLock().lock();
        try {
            return new ArrayList<>(this.deltaList.subList(Math.min(Math.max(index, 0),
                    this.deltaList.size()), this.deltaList.size()));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Aborts the current input
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onAbortInput(final AbortInputOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.inputMessage = null;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Adds the delta to the end of the deltaList
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onAddDelta(final AddDeltaOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            deltaList.add(operation.getDelta());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Adds the deltas to the end of the deltaList
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onAddDeltas(final AddDeltasOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            deltaList.addAll(operation.getDeltaList());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Requests a new input by setting the inputMessage
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onRequestInput(final RequestInputOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.inputMessage = operation.getMessage();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Sets canUndo
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onCanUndoChanged(final CanUndoChangedOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.canUndo = operation.isCanUndo();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Sets canRedo
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onCanRedoChanged(final CanRedoChangedOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.canRedo = operation.isCanRedo();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Sets the current speed
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onSpeedChanged(final SpeedChangedOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.speed = operation.getSpeed();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires operation != null;
     @ requires isAlive();
     @*/
    /**
     * Sets the current mode
     * @param operation the operation from the client, must be != null
     * @throws IllegalStateException if this session isn't alive
     */
    private void onModeChanged(final ModeChangedOperation operation) {
        checkNotNull(operation, "operation must be != null");
        checkState(isAlive(), "session must not be stopped");

        readWriteLock.writeLock().lock();
        try {
            this.mode = operation.getMode();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*@
     @ requires isAlive();
     @ requires this.canUndo;
     @*/
    /**
     * Undoes a single command
     * This does not have an immediate effect because it is sent to
     * the client.
     * @throws IllegalStateException if this session isn't alive or if undo is not possible
     */
    public void undo() {
        checkState(isAlive(), "session must not be stopped");
        checkState(this.canUndo, "cannot undo");

        sendOperation(new UndoOperation());
    }

    /*@
     @ requires isAlive();
     @ requires this.canRedo;
     @*/
    /**
     * Undoes a single command
     * This does not have an immediate effect because it is sent to
     * the client.
     * @throws IllegalStateException if this session isn't alive or if redo is not possible
     */
    public void redo() {
        checkState(isAlive(), "session must not be stopped");
        checkState(this.canRedo, "cannot redo");

        sendOperation(new RedoOperation());
    }

    /*@
     @ requires isAlive();
     @ requires this.mode == Mode.PAUSED;
     @*/
    /**
     * Resumes the execution of the game
     * This does not have an immediate effect because it is sent to
     * the client.
     * @throws IllegalStateException if this session isn't alive or if the game is not paused
     */
    public void resume() {
        checkState(isAlive(), "session must not be stopped");
        checkState(this.mode == Mode.PAUSED, "cannot resume in non paused mode");

        sendOperation(new ResumeOperation());
    }

    /*@
     @ requires isAlive();
     @ requires this.mode == Mode.RUNNING;
     @*/
    /**
     * Pauses the execution of the game
     * This does not have an immediate effect because it is sent to
     * the client.
     * @throws IllegalStateException if this session isn't alive or if the game is not running
     */
    public void pause() {
        checkState(isAlive(), "session must not be stopped");
        checkState(this.mode == Mode.RUNNING, "cannot resume in non running mode");

        sendOperation(new PauseOperation());
    }

    /*@
     @ requires isAlive();
     @ requires (this.inputMessage != null) && (this.inputMessage.getInputId() == inputId);
     @*/
    /**
     * Resolves the current input request
     * This does not have an immediate effect because it is sent to
     * the client.
     * @param inputId the id of the input request which should be resolved, used to check that
     *                no outdated request resolves the current one
     * @param result the result of the input request, might be null
     * @throws IllegalStateException if this session isn't alive, no input is requested
     *                               or if inputId does not mach the current request
     */
    public void setInputResult(final int inputId, final String result) {
        this.readWriteLock.readLock().lock();
        try {
            checkState(isAlive(), "session must not be stopped");
            checkState(this.inputMessage != null, "no input requested");
            checkState(this.inputMessage.getInputId() == inputId,
                    "inputId does not match current input request");

            sendOperation(new SetInputOperation(inputId, result));
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }


    /*@
     @ requires isAlive();
     @ requires (speed >= 0) && (speed <= 10);
     @*/
    /**
     * Changes the speed of the game
     * This does not have an immediate effect because it is sent to
     * the client.
     * @throws IllegalStateException if this session isn't alive
     * @throws IllegalArgumentException if speed is not in range [0, 10]
     */
    public void changeSpeed(final double speed) {
        checkState(isAlive(), "session must not be stopped");
        checkArgument((speed >= 0) && (speed <= 10), "Provided speed is not in range [0, 10]");

        sendOperation(new ChangeSpeedOperation(speed));
    }

    /*@
     @ requires true;
     @ pure;
     @ ensures \result != null;
     @*/
    /**
     * Gets the current status of the session. This is immutable and therefore does
     * not reflect future changes.
     * Use with care: this keeps the session alive because it signals interest in this
     * session
     * @param since the index since which deltas should be included
     * @return the current status of the game
     */
    public GameState getGameState(final int since) {
        keepAlive();

        this.readWriteLock.readLock().lock();
        try {
            return new GameState(mode, inputMessage, canUndo, canRedo, speed, getDeltasSince(since));
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    /*@
     @ requires true;
     @ pure;
     @ ensures \result != null;
     @*/
    /**
     * Gets the current input message which, which reflects
     * which input the session expects. If no input is expected, this returns an
     * empty optional
     * @return the current InputMessage, or an empty optional if not present
     */
    public Optional<InputMessage> getInputMessage() {
        keepAlive();

        this.readWriteLock.readLock().lock();
        try {
            return Optional.ofNullable(inputMessage);
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    /*@
     @ requires true;
     @ pure;
     @*/
    /**
     * Checks if this session is still alive
     * If it isn't alive, the socket is closed and all non-damon threads are stopped
     * @return true if this session hasn't been shut down
     */
    public boolean isAlive() {
        return this.isAlive;
    }

    /*@
     @ requires true;
     @ pure;
     @*/
    /**
     * Getter for the id
     * @return the id of this session as provided in the constructor
     */
    public int getId() {
        return id;
    }
}
