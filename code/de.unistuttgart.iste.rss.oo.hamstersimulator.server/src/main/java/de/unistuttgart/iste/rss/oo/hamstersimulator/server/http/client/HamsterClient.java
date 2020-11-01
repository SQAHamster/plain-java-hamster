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
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.TileContentType;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server.HamsTTPServer;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.RemoteInputInterface;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public final class HamsterClient {

    private final Map<ObservableTileContent, Integer> contentIdRelation = new IdentityHashMap<>();
    private final Map<ObservableHamster, ChangeListener<Direction>> hamsterDirectionChangeListenerRelation = new IdentityHashMap<>();
    private volatile int idCounter = 0;

    private final LambdaVisitor<Operation, Runnable> operationVisitor;

    private final HamsterGameController gameController;
    private final RemoteInputInterface inputInterface;
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    private HamsterClient(final HamsterGameViewModel gameViewModel, final RemoteInputInterface inputInterface,
                          final int port) throws IOException {
        this.gameController = gameViewModel.getGameController();
        this.inputInterface = inputInterface;
        addInputMessageListener(inputInterface);

        this.socket = new Socket("127.0.0.1", port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        startInputListener(socket);

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

    private void addInputMessageListener(final RemoteInputInterface inputInterface) {
        inputInterface.messageProperty().addListener((observableValue, oldMessage, newMessage) -> {
            sendOperation(new RequestInputOperation(newMessage.orElse(null)));
        });
    }

    public static void startAndConnectToServer(final HamsterGameViewModel gameViewModel) {
        try {
            HamsTTPServer.startIfNotRunning();
            final RemoteInputInterface inputInterface = new RemoteInputInterface();
            final HamsterClient client = new HamsterClient(gameViewModel, inputInterface, 8008);
            gameViewModel.addInputInterface(inputInterface);
        } catch (IOException e) {
            throw new RuntimeException("failed to start client", e);
        }
    }

    public static void startAndConnectToServerOnPort(final HamsterGameViewModel gameViewModel,
                                                     final InetAddress httpServerInetAddress, final int httpServerPort, final int port) throws IOException {
        HamsTTPServer.startOnPort(httpServerInetAddress, httpServerPort, port);
        final RemoteInputInterface inputInterface = new RemoteInputInterface();
        final HamsterClient client = new HamsterClient(gameViewModel, inputInterface, port);
        gameViewModel.addInputInterface(inputInterface);
    }

    private void startInputListener(final Socket socket) throws IOException {
        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        new Thread(() -> {
            try {
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
            } catch (Exception e) {
                shutdown();
            }
        }).start();
    }

    private void initListeners(final HamsterGameViewModel gameViewModel) {
        gameViewModel.getLog().logProperty().addListener(this::onLogChanged);
        initTerritoryListeners(gameViewModel.getTerritory());
        initHamsterListeners(gameViewModel.getTerritory());
        initGameControllerListeners(gameViewModel.getGameController());
    }

    private void initTerritoryListeners(final ObservableTerritory territory) {
        initTileListeners(territory);
        territory.territorySizeProperty().addListener((observableValue, oldSize, newSize) -> {
            sendOperation(new AddDeltaOperation(new NewTerritoryDelta(newSize)));
        });
    }

    private void initTileListeners(final ObservableTerritory territory) {
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

    private void initHamsterListeners(final ObservableTerritory territory) {
        territory.hamstersProperty().addListener((ListChangeListener<ObservableHamster>) change -> {
            change.getAddedSubList().forEach(this::hamsterAdded);
            change.getRemoved().forEach(this::hamsterRemoved);
        });
        territory.hamstersProperty().forEach(this::hamsterAdded);
        territory.hamstersProperty().addListener(this::onHamsterAdded);
    }

    private void hamsterAdded(final ObservableHamster hamster) {
        final ChangeListener<Direction> hamsterDirectionChangeListener = createHamsterDirectionChangeListener(hamster);
        hamsterDirectionChangeListenerRelation.put(hamster, hamsterDirectionChangeListener);
        hamster.directionProperty().addListener(hamsterDirectionChangeListener);
    }

    private void hamsterRemoved(final ObservableHamster hamster) {
        hamster.directionProperty().removeListener(hamsterDirectionChangeListenerRelation.get(hamster));
        hamsterDirectionChangeListenerRelation.remove(hamster);
    }

    private ChangeListener<Direction> createHamsterDirectionChangeListener(final ObservableHamster hamster) {
        return (observable, oldValue, newValue) -> {
            onHamsterDirectionChanged(hamster, newValue);
        };
    }

    private void initGameControllerListeners(final HamsterGameController gameController) {
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

    private void initInitialState(final HamsterGameViewModel gameViewModel) {
        final List<Delta> deltas = new ArrayList<>();
        initInitialTerritoryState(gameViewModel.getTerritory(), deltas);
        initInitialLogState(gameViewModel.getLog(), deltas);
        initInitialGameControllerState(gameViewModel.getGameController());
        sendOperation(new AddDeltasOperation(deltas));
    }

    private void initInitialTerritoryState(final ObservableTerritory territory, final List<Delta> deltas) {
        deltas.add(new NewTerritoryDelta(territory.getSize()));
        territory.tilesProperty().stream().flatMap(tile -> tile.contentProperty().stream())
                .forEach(content -> deltas.add(addedTileContent(content)));
        territory.hamstersProperty().forEach(hamster -> {
            deltas.add(new RotateHamsterDelta(contentIdRelation.get(hamster), hamster.getDirection()));
        });
    }

    private void initInitialLogState(final ObservableLog log, final List<Delta> deltas) {
        log.logProperty().forEach(logEntry -> deltas.add(addedLogEntry(logEntry)));
    }

    private void initInitialGameControllerState(final HamsterGameController gameController) {
        sendOperation(new ModeChangedOperation(gameController.modeProperty().get()));
        sendOperation(new CanUndoChangedOperation(gameController.canUndoProperty().get()));
        sendOperation(new CanRedoChangedOperation(gameController.canRedoProperty().get()));
        sendOperation(new SpeedChangedOperation(gameController.speedProperty().get()));
    }

    private synchronized void sendOperation(final Operation operation) {
        try {
            this.outputStream.writeObject(operation);
        } catch (IOException e) {
            shutdown();
        }
    }

    private synchronized void onLogChanged(ListChangeListener.Change<? extends ObservableLogEntry> change) {
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
    }

    private synchronized void onTileContentsChanged(final ListChangeListener.Change<? extends ObservableTileContent> change) {
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
    }

    private synchronized void onHamsterDirectionChanged(final ObservableHamster hamster, final Direction newDirection) {
        sendOperation(new AddDeltaOperation(new RotateHamsterDelta(contentIdRelation.get(hamster), newDirection)));
    }

    private synchronized void onHamsterAdded(final ListChangeListener.Change<? extends ObservableHamster> change) {
        final List<Delta> deltas = new ArrayList<>();
        while (change.next()) {
            for (final ObservableHamster hamster : change.getAddedSubList()) {
                deltas.add(new RotateHamsterDelta(contentIdRelation.get(hamster), hamster.getDirection()));
            }
        }
        sendOperation(new AddDeltasOperation(deltas));
    }

    private synchronized Delta addedTileContent(final ObservableTileContent addedContent) {
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
    }

    private synchronized Delta addedLogEntry(final ObservableLogEntry logEntry) {
        return new AddLogEntryDelta(logEntry.getMessage(),
                contentIdRelation.get(logEntry.getHamster()));
    }


    private void onChangeSpeed(final ChangeSpeedOperation operation) {
        this.gameController.setSpeed(operation.getSpeed());
    }

    private void onPause(final PauseOperation pauseOperation) {
        if (gameController.modeProperty().get() == Mode.RUNNING) {
            gameController.pauseAsync();
        }
    }

    private void onResume(final ResumeOperation operation) {
        if (gameController.modeProperty().get() == Mode.PAUSED) {
            gameController.resume();
        }
    }

    private void onRedo(final RedoOperation redoOperation) {
        if (gameController.canRedoProperty().get()) {
            gameController.redo();
        }
    }

    private void onUndo(final UndoOperation operation) {
        if (gameController.canUndoProperty().get()) {
            gameController.undo();
        }
    }

    private void onAbort(final AbortOperation operation) {
        gameController.abortOrStopGame();
    }


    private void onSetInput(final SetInputOperation operation) {
        if (this.inputInterface.getInputID() == operation.getInputId()) {
            this.inputInterface.setResult(operation.getResult(), operation.getInputId());
            sendOperation(new AbortInputOperation(operation.getInputId()));
        }
    }

    private void shutdown() {
        try {
            this.socket.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
