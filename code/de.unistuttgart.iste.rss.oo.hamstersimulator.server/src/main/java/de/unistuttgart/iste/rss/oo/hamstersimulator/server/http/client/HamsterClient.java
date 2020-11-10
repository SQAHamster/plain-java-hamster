package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameController;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.SpeedChangedOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.InputMode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.TileContentType;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server.HamsterHTTPServer;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.RemoteInputInterface;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

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
public final class HamsterClient {

    /**
     * Map with all TilContents, each TileContent has a specific id
     */
    private final Map<ObservableTileContent, Integer> contentIdRelation = new IdentityHashMap<>();
    /**
     * Map with listeners for each hamsters
     * If a hamster is removed from the territory, the listener is also removed
     */
    private final Map<ObservableHamster, ChangeListener<Direction>> hamsterDirectionChangeListenerRelation = new IdentityHashMap<>();
    /**
     * counter for the TileContent id
     */
    private volatile int idCounter = 0;

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
                .on(AbortOperation.class).then(operation -> () -> onAbort(operation));

        initListeners(gameViewModel);
        initInitialState(gameViewModel);
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
     @ requires gameViewModel != null;
     @*/
    /**
     * Initializes all change listeners on the Territory and HamsterGameController
     * @param gameViewModel the GameViewModel on which all listeners should be added
     */
    private void initListeners(final HamsterGameViewModel gameViewModel) {
        checkNotNull(gameViewModel);

        gameViewModel.getLog().logProperty().addListener(this::onLogChanged);
        initTerritoryListeners(gameViewModel.getTerritory());
        initHamsterListeners(gameViewModel.getTerritory());
        initGameControllerListeners(gameViewModel.getGameController());
    }

    /*@
     @ requires territory != null;
     @*/
    /**
     * Initializes all change listeners on the territory directly
     * @param territory the ObservableTerritory on which all listeners should be added
     */
    private void initTerritoryListeners(final ObservableTerritory territory) {
        checkNotNull(territory);

        initTileListeners(territory);
        territory.territorySizeProperty().addListener((observableValue, oldSize, newSize) -> {
            sendOperation(new AddDeltaOperation(new NewTerritoryDelta(newSize)));
        });
    }

    /*@
     @ requires territory != null;
     @*/
    /**
     * Initializes all change listeners on all tiles of the territory
     * @param territory the ObservableTerritory on which all listeners should be added
     */
    private void initTileListeners(final ObservableTerritory territory) {
        checkNotNull(territory);

        final ListChangeListener<ObservableTileContent> tileContentsChangedListener = this::onTileContentsChanged;
        territory.tilesProperty().addListener((ListChangeListener<ObservableTile>) change -> {
            for (final ObservableTile addedTile : change.getAddedSubList()) {
                addedTile.contentProperty().addListener(tileContentsChangedListener);
            }
            for (final ObservableTile removedTile : change.getRemoved()) {
                removedTile.contentProperty().removeListener(tileContentsChangedListener);
            }
        });
        territory.tilesProperty().forEach(tile -> tile.contentProperty().addListener(tileContentsChangedListener));
    }

    /*@
     @ requires territory != null;
     @*/
    /**
     * Initializes all change listeners on the territory which listen for added and removed hamsters
     * @param territory the ObservableTerritory on which all listeners should be added
     */
    private void initHamsterListeners(final ObservableTerritory territory) {
        checkNotNull(territory);

        territory.hamstersProperty().addListener((ListChangeListener<ObservableHamster>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(this::hamsterAdded);
                change.getRemoved().forEach(this::hamsterRemoved);
            }
        });
        territory.hamstersProperty().forEach(this::hamsterAdded);
        territory.hamstersProperty().addListener(this::onHamsterAdded);
    }

    /*@
     @ requires hamster != null;
     @ ensures hamsterDirectionChangeListenerRelation.containsKey(hamster);
     @*/
    /**
     * Called when a hamster is added to the territory
     * Adds the direction listener to the hamster
     * @param hamster the hamster on which the listener should be added
     */
    private void hamsterAdded(final ObservableHamster hamster) {
        checkNotNull(hamster);

        final ChangeListener<Direction> hamsterDirectionChangeListener = (observable, oldValue, newValue) -> {
            onHamsterDirectionChanged(hamster, newValue);
        };
        hamsterDirectionChangeListenerRelation.put(hamster, hamsterDirectionChangeListener);
        hamster.directionProperty().addListener(hamsterDirectionChangeListener);
    }

    /*@
     @ requires hamster != null;
     @ ensures !hamsterDirectionChangeListenerRelation.containsKey(hamster);
     @*/
    /**
     * Called when a hamster is removed from the territory
     * Removes the direction listener from the hamster
     * @param hamster the hamster on which the listener should be removed
     */
    private void hamsterRemoved(final ObservableHamster hamster) {
        checkNotNull(hamster);

        hamster.directionProperty().removeListener(hamsterDirectionChangeListenerRelation.get(hamster));
        hamsterDirectionChangeListenerRelation.remove(hamster);
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
     @ requires gameViewModel != null;
     @*/
    /**
     * Sends an AddDeltasOperation to the server which contains all deltas which make
     * up the initial game state
     * @param gameViewModel the viewModel which should be serialized into deltas
     */
    private void initInitialState(final HamsterGameViewModel gameViewModel) {
        checkNotNull(gameViewModel);

        final List<Delta> deltas = new ArrayList<>();
        initInitialTerritoryState(gameViewModel.getTerritory(), deltas);
        initInitialLogState(gameViewModel.getLog(), deltas);
        initInitialGameControllerState(gameViewModel.getGameController());
        sendOperation(new AddDeltasOperation(deltas));
    }

    /*@
     @ requires territory != null;
     @ requires deltas != null;
     @*/
    /**
     * Serializes the state of the territory into deltas which are added to the deltas list
     * @param territory the territory to serialize into deltas
     * @param deltas list where to add the deltas
     */
    private void initInitialTerritoryState(final ObservableTerritory territory, final List<Delta> deltas) {
        checkNotNull(territory);
        checkNotNull(deltas);

        deltas.add(new NewTerritoryDelta(territory.getSize()));
        territory.tilesProperty().stream().flatMap(tile -> tile.contentProperty().stream())
                .forEach(content -> deltas.add(addedTileContent(content)));
        territory.hamstersProperty().forEach(hamster -> {
            deltas.add(new RotateHamsterDelta(contentIdRelation.get(hamster), hamster.getDirection()));
        });
    }

    /*@
     @ requires log != null;
     @ requires deltas != null;
     @*/
    /**
     * Serializes all log messages into deltas and adds
     * @param log the log to serialize into deltas
     * @param deltas list where to add the deltas
     */
    private void initInitialLogState(final ObservableLog log, final List<Delta> deltas) {
        checkNotNull(log);
        checkNotNull(deltas);

        log.logProperty().forEach(logEntry -> deltas.add(addedLogEntry(logEntry)));
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
     @ requires change != null;
     @*/
    /**
     * Called when the logProperty on ObservableLog is changed
     * Handles added and removed log entries
     * @param change the change which describes how the property changed
     */
    private void onLogChanged(final ListChangeListener.Change<? extends ObservableLogEntry> change) {
        this.lock.lock();
        try {
            checkNotNull(change);

            final List<Delta> deltas = new ArrayList<>();
            while (change.next()) {
                if (change.wasAdded() && change.wasRemoved()) {
                    throw new IllegalStateException("add and remove in one change is not supported ");
                }
    
                for (final ObservableLogEntry logEntry : change.getAddedSubList()) {
                    deltas.add(addedLogEntry(logEntry));
                }
                for (final ObservableLogEntry logEntry : change.getRemoved()) {
                    deltas.add(new RemoveLogEntryDelta());
                }
            }
            sendOperation(new AddDeltasOperation(deltas));
        } finally {
            this.lock.unlock();
        }
    }

    /*@
     @ requires change != null;
     @*/
    /**
     * Called when the content property on ObservableTile is changed
     * Handles added and removed TileContents
     * @param change the change which describes how the property changed
     */
    private void onTileContentsChanged(final ListChangeListener.Change<? extends ObservableTileContent> change) {
        this.lock.lock();
        try {
            checkNotNull(change);

            final List<Delta> deltas = new ArrayList<>();
            while (change.next()) {
                for (final ObservableTileContent addedContent : change.getAddedSubList()) {
                    deltas.add(addedTileContent(addedContent));
                }
                for (final ObservableTileContent removedContent : change.getRemoved()) {
                    deltas.add(new RemoveTileContentDelta(contentIdRelation.get(removedContent)));
                }
            }
            sendOperation(new AddDeltasOperation(deltas));
        } finally {
            this.lock.unlock();
        }
    }

    /*@
     @ requires hamster != null;
     @ requires newDirection != null;
     @ requires contentIdRelation.containsKey(hamster);
     */
    /**
     * Called when the direction of the hamster is changed
     * @param hamster the hamster whose direction changed
     * @param newDirection the hamster's new direction
     * @throws IllegalStateException if the hamster was not added to the territory
     */
    private void onHamsterDirectionChanged(final ObservableHamster hamster, final Direction newDirection) {
        this.lock.lock();
        try {
            checkNotNull(hamster);
            checkNotNull(newDirection);
            checkState(contentIdRelation.containsKey(hamster));
            
            sendOperation(new AddDeltaOperation(new RotateHamsterDelta(contentIdRelation.get(hamster), newDirection)));
        } finally {
            this.lock.unlock();
        }
    }

    /*@
     @ requires change != null;
     @*/
    /**
     * Called when the hamsters property on ObservableTerritory is changed
     * only handles additions, ignores removals
     * @param change the change which describes how the property changed
     */
    private void onHamsterAdded(final ListChangeListener.Change<? extends ObservableHamster> change) {
        this.lock.lock();
        try {
            checkNotNull(change);

            final List<Delta> deltas = new ArrayList<>();
            while (change.next()) {
                for (final ObservableHamster hamster : change.getAddedSubList()) {
                    deltas.add(new RotateHamsterDelta(contentIdRelation.get(hamster), hamster.getDirection()));
                }
            }
            sendOperation(new AddDeltasOperation(deltas));
        } finally {
            this.lock.unlock();
        }
    }

    /*@
     @ requires addedContent != null;
     @ ensures contentIdRelation.containsKey(addedContent);
     @ ensures \result != null;
     @*/
    /**
     * Called when an ObservableTileContent was added to a tile
     * Adds the content to the contentIdRelation if necessary
     * Note: the new location of the addedContent should already be set
     * @param addedContent the ObservableTileContent which was added
     * @return the delta which can be sent to the server
     */
    private Delta addedTileContent(final ObservableTileContent addedContent) {
        this.lock.lock();
        try {
            checkNotNull(addedContent);

            if (contentIdRelation.containsKey(addedContent)) {
                return new AddTileContentDelta(TileContentType.fromObservable(addedContent),
                        addedContent.getCurrentLocation().orElseThrow(), contentIdRelation.get(addedContent));
            } else {
                contentIdRelation.put(addedContent, idCounter);
                final Delta delta = new AddTileContentDelta(TileContentType.fromObservable(addedContent),
                        addedContent.getCurrentLocation().orElseThrow(), idCounter);
                idCounter++;
                return delta;
            }
        } finally {
            this.lock.unlock();
        }
    }

    /*@
     @ requires logEntry != null;
     @ requires (logEntry.getHamster() != null) ==> contentIdRelation.containsKey(logEntry.getHamster());
     @ ensures \result != null;
     @*/
    /**
     * Called when an ObservableLogEntry is added to the log
     * @param logEntry the ObservableLogEntry which was added to the log
     * @return the delta which can be sent to the server
     * @throws IllegalArgumentException if the hamster associated with the log entry is unknown
     */
    private Delta addedLogEntry(final ObservableLogEntry logEntry) {
        this.lock.lock();
        try {
            checkNotNull(logEntry);
            checkArgument(logEntry.getHamster() == null || contentIdRelation.containsKey(logEntry.getHamster()));
    
            return new AddLogEntryDelta(logEntry.getMessage(),
                    contentIdRelation.get(logEntry.getHamster()));
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
}
