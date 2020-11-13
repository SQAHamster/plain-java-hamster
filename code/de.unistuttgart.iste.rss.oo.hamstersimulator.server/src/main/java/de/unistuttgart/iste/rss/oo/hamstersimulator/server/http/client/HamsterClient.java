package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameController;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.AbortInputOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.SpeedChangedOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.InputMode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server.HamsterHTTPServer;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.RemoteInputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.observer.DeltaListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.observer.TerritoryLogObserver;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static de.unistuttgart.iste.rss.utils.Preconditions.*;

/**
 * The HamsterClient connects to the HamsterServer.
 * It represents a single HamsterGame on the client side.
 * It listens on the provided RemoteInputInterface for new input messages
 */
public final class HamsterClient implements DeltaListener {

    /**
     * lambda visitor which handles all incoming operations from the server
     */
    private final LambdaVisitor<Operation, Runnable> operationVisitor;

    /**
     * GameController used to handle actions like pause/undo/redo etc.
     */
    private final HamsterGameController gameController;
    /**
     * InputInterface used to handle user input
     */
    private final RemoteInputInterface inputInterface;
    /**
     * Socket used to communicate with the server
     */
    private final Socket socket;
    /**
     * Stream used to send messages to the server
     */
    private final ObjectOutputStream outputStream;
    /**
     * Lock used for synchronization
     */
    private final ReentrantLock lock = new ReentrantLock(true);

    /*@
     @ requires gameViewModel != null;
     @ requires inputInterface != null;
     @ requires (port > 0) && (port <= 65535);
     @*/
    /**
     * Creates a new HamsterClient with the specified server port
     * The server must be reachable on 127.0.0.1
     * @param gameViewModel ViewModel which represents the HamsterGame and is
     *                      used to interact with the HamsterGame
     * @param inputInterface RemoteInputInterface which should be registered on gameViewModel and which is
     *                       used to handle input
     * @param port the port where the server is running, must be a valid port
     * @throws IOException if it is not possible to start the client because the server is not reachable
     * @throws IllegalArgumentException if port is no allowed TCP port
     */
    public HamsterClient(final HamsterGameViewModel gameViewModel, final RemoteInputInterface inputInterface,
                          final int port) throws IOException {
        checkNotNull(gameViewModel);
        checkNotNull(inputInterface);
        checkArgument((port > 0) && (port <= 65535));

        this.gameController = gameViewModel.getGameController();
        this.inputInterface = inputInterface;
        addInputMessageListener(inputInterface);

        this.socket = new Socket("127.0.0.1", port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        startOperationsListener(socket);

        operationVisitor = new LambdaVisitor<Operation, Runnable>()
                .on(ChangeSpeedOperation.class).then(operation -> () -> onChangeSpeed(operation))
                .on(PauseOperation.class).then(operation -> () -> onPause(operation))
                .on(ResumeOperation.class).then(operation -> () -> onResume(operation))
                .on(RedoOperation.class).then(operation -> () -> onRedo(operation))
                .on(SetInputOperation.class).then(operation -> () -> onSetInput(operation))
                .on(UndoOperation.class).then(operation -> () -> onUndo(operation))
                .on(AbortOperation.class).then(operation -> () -> onAbort(operation))
                .on(AbortInputOperation.class).then(operation -> () -> onAbortInput(operation));

        initGameControllerListeners(gameController);
        initInitialGameControllerState(gameController);

        new TerritoryLogObserver(gameViewModel.getTerritory(), gameViewModel.getLog(), this);
    }

    /*@
     @ requires inputInterface != null;
     @*/
    /**
     * Adds a listener on the inputInterface which listens for new messages
     * @param inputInterface the RemoteInputInterface used to listen for new input messages
     */
    private void addInputMessageListener(final RemoteInputInterface inputInterface) {
        checkNotNull(inputInterface);

        inputInterface.messageProperty().addListener((observableValue, oldMessage, newMessage) -> {
            sendOperation(new RequestInputOperation(newMessage.orElse(null)));
        });
    }

    /*@
     @ requires gameViewModel != null;
     @*/
    /**
     * Starts the server on the default ports if it is not already running, and creates a new
     * HamsterClient and connects it to the server
     * @param gameViewModel the ViewModel which represents the HamsterGame, used by the client
     *                      to handle all input/output
     * @throws RuntimeException if the client failed to connect to the server
     */
    public static void startAndConnectToServer(final HamsterGameViewModel gameViewModel) {
        checkNotNull(gameViewModel);

        try {
            HamsterHTTPServer.startIfNotRunning();
            final RemoteInputInterface inputInterface = new RemoteInputInterface();
            final HamsterClient client = new HamsterClient(gameViewModel, inputInterface, 8008);
            gameViewModel.addInputInterface(inputInterface);
        } catch (IOException e) {
            throw new RuntimeException("failed to start client", e);
        }
    }

    /*@
     @ requires gameViewModel != null;
     @ requires httpServerInetAddress != null;
     @ requires (httpServerPort > 0) && (httpServerPort <= 65535);
     @ requires (port > 0) && (port <= 65535);
     @*/
    /**
     * Starts the HamsTTPServer on the specified address and ports. If it is already running (and therefore
     * the port is blocked), it throws an IOException.
     * Use with care! Most UIs probably expect the server running on the default ports
     * @param gameViewModel the ViewModel which represents the HamsterGame, used by the client
     *                      to handle all input/output
     * @param httpServerInetAddress the InetAddress on which the HttpServer listens for incoming HTTP requests
     * @param httpServerPort the port on which the HttpServer listens for incoming HTTP request
     * @param port the port on which the server listens for new HamsterClients
     * @throws IOException if it is not possible to start the server or if the client failed to connect to the server
     * @throws IllegalArgumentException if port or httpServerPort is no allowed TCP port
     */
    public static void startAndConnectToServerOnPort(final HamsterGameViewModel gameViewModel,
                                                     final InetAddress httpServerInetAddress,
                                                     final int httpServerPort, final int port) throws IOException {
        checkNotNull(gameViewModel);
        checkNotNull(httpServerInetAddress);
        checkArgument((httpServerPort > 0) && (httpServerPort <= 65535));
        checkArgument((port > 0) && (port <= 65535));

        HamsterHTTPServer.startOnPort(httpServerInetAddress, httpServerPort, port);
        final RemoteInputInterface inputInterface = new RemoteInputInterface();
        final HamsterClient client = new HamsterClient(gameViewModel, inputInterface, port);
        gameViewModel.addInputInterface(inputInterface);
    }

    /*@
     @ requires socket != null;
     @ requires !socket.isClosed();
     @*/
    /**
     * Starts to listen for operations from the server on a new thread.
     * This should only be called once with a socket. If the socket is closed, the thread
     * is terminated and the shutdown initiated
     * @param socket the socket used to create the ObjectInputStream
     * @throws IOException if it is not possible to create the ObjectInputStream
     * @throws IllegalArgumentException if the socket is already closed
     */
    private void startOperationsListener(final Socket socket) throws IOException {
        checkNotNull(socket);
        checkArgument(!socket.isClosed());

        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        new Thread(() -> {
            try {
               listenForOperations(socket, inputStream);
            } catch (Exception e) {
                shutdown();
            }
        }).start();
    }

    /*@
     @ requires socket != null;
     @ requires inputStream != null;
     @ requires !socket.isClosed();
     @*/
    /**
     * Listens blocking for new operations from the server
     * @param socket the socket which is used for the inputStream, used to check if it is still open
     * @param inputStream used to receive and decode operations from the client
     * @throws IOException if the socket was closed while listening for operations
     * @throws ClassNotFoundException if the client send an operation which is unknown to the server
     * @throws IllegalArgumentException if the socket is already closed
     */
    private void listenForOperations(final Socket socket, final ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException {
        checkNotNull(socket);
        checkNotNull(inputStream);
        checkArgument(!socket.isClosed());

        while (!socket.isClosed()) {
            final Object input = inputStream.readObject();
            final Runnable handler = operationVisitor.apply(input);
            if (handler == null) {
                throw new IllegalStateException("no handler found for the operation");
            } else {
                try {
                    handler.run();
                } catch (RuntimeException e) {
                    // due to multithreading, we cannot prevent some exceptions from sometimes happen
                    // we do not want to stop the game because of this, so we just ignore these
                }
            }
        }
    }

    /*@
     @ requires gameController != null;
     @*/
    /**
     * Sends operations to the server which set the correct initial state of the HamsterGameController
     * @param gameController the HamsterGameController whose the state should be sent to the server
     */
    private void initInitialGameControllerState(final HamsterGameController gameController) {
        checkNotNull(gameController);

        sendOperation(new ModeChangedOperation(gameController.modeProperty().get()));
        sendOperation(new CanUndoChangedOperation(gameController.canUndoProperty().get()));
        sendOperation(new CanRedoChangedOperation(gameController.canRedoProperty().get()));
        sendOperation(new SpeedChangedOperation(gameController.speedProperty().get()));
    }

    /*@
     @ requires territory != null;
     @*/
    /**
     * Initializes all change listeners on the HamsterGameController
     * @param gameController the HamsterGameController on which all listeners should be added
     */
    private void initGameControllerListeners(final HamsterGameController gameController) {
        checkNotNull(gameController);

        gameController.canRedoProperty().addListener((observable, oldValue, newValue) -> {
            sendOperation(new CanRedoChangedOperation(newValue));
        });
        gameController.canUndoProperty().addListener((observable, oldValue, newValue) -> {
            sendOperation(new CanUndoChangedOperation(newValue));
        });
        gameController.speedProperty().addListener((observable, oldValue, newValue) -> {
            sendOperation(new SpeedChangedOperation(newValue.doubleValue()));
        });
        gameController.modeProperty().addListener((observable, oldValue, newValue) -> {
            sendOperation(new ModeChangedOperation(newValue));
        });
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * Sends an operation to the server via the the outputStream
     * if this is not possible, the shutdown is initiated.
     * @param operation the Operation which is sent to the client, must be != null
     */
    private void sendOperation(final Operation operation) {
        this.lock.lock();
        try {
            this.outputStream.writeObject(operation);
        } catch (IOException e) {
            shutdown();
        } finally {
            this.lock.unlock();
        }
    }



    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's change speed request
     * @param operation the operation from the server
     */
    private void onChangeSpeed(final ChangeSpeedOperation operation) {
        checkNotNull(operation);

        this.gameController.setSpeed(operation.getSpeed());
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's pause request
     * if pause is not possible, it does nothing
     * @param operation the operation from the server
     */
    private void onPause(final PauseOperation operation) {
        checkNotNull(operation);

        if (gameController.modeProperty().get() == Mode.RUNNING) {
            gameController.pauseAsync();
        }
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's resume request
     * if resume is not possible, it does nothing
     * @param operation the operation from the server
     */
    private void onResume(final ResumeOperation operation) {
        checkNotNull(operation);

        if (gameController.modeProperty().get() == Mode.PAUSED) {
            gameController.resume();
        }
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's redo request
     * if redo is not possible, it does nothing
     * @param operation the operation from the server
     */
    private void onRedo(final RedoOperation operation) {
        checkNotNull(operation);

        if (gameController.canRedoProperty().get()) {
            gameController.redo();
        }
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's set input request
     * if undo is not possible, it does nothing
     * @param operation the operation from the server
     */
    private void onUndo(final UndoOperation operation) {
        checkNotNull(operation);

        if (gameController.canUndoProperty().get()) {
            gameController.undo();
        }
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's abort request
     * @param operation the operation from the server
     */
    private void onAbort(final AbortOperation operation) {
        checkNotNull(operation);

        gameController.abortOrStopGame();
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's abort input request
     * @param operation the operation from the server
     */
    private void onAbortInput(final AbortInputOperation operation) {
        checkNotNull(operation);

        if (this.inputInterface.getInputID() == operation.getInputId()
                && this.inputInterface.getInputMode() != InputMode.NONE) {
            this.inputInterface.setResultNoInput(operation.getInputId());
            sendOperation(new AbortInputOperation(operation.getInputId()));
        }
    }

    /*@
     @ requires operation != null;
     @*/
    /**
     * handles the server's set input request
     * if the request is outdated or no input is requested it does nothing
     * @param operation the operation from the server
     */
    private void onSetInput(final SetInputOperation operation) {
        checkNotNull(operation);

        if (this.inputInterface.getInputID() == operation.getInputId()
                && this.inputInterface.getInputMode() != InputMode.NONE) {
            this.inputInterface.setResult(operation.getResult(), operation.getInputId());
            sendOperation(new AbortInputOperation(operation.getInputId()));
        }
    }

    /**
     * Shuts the client down
     * Closes the socket
     */
    private void shutdown() {
        try {
            this.socket.close();
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * Should be called by a TerritoryObserver when new deltas are added
     *
     * @param deltaList a list with all new deltas, != null
     */
    @Override
    public void onDeltasCreated(List<Delta> deltaList) {
        sendOperation(new AddDeltasOperation(deltaList));
    }
}
