package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameController;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.SpeedChangedOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.TileContentType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;

import java.util.*;

public class HamsterHttpClient {

    private final Map<ObservableTileContent, Integer> contentIdRelation = new IdentityHashMap<>();
    private final Map<ObservableHamster, ChangeListener<Direction>> hamsterDirectionChangeListenerRelation = new IdentityHashMap<>();
    private volatile int idCounter = 0;

    public HamsterHttpClient(final HamsterGameViewModel gameViewModel) {
        initListeners(gameViewModel);
        initInitialState(gameViewModel);
    }

    private void initListeners(final HamsterGameViewModel gameViewModel) {
        gameViewModel.getLog().logProperty().addListener(this::onLogChanged);
        initTileListeners(gameViewModel.getTerritory());
        initHamsterListeners(gameViewModel.getTerritory());
        initGameControllerListeners(gameViewModel.getGameController());
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
        //TODO
    }

    private synchronized void onLogChanged(ListChangeListener.Change<? extends ObservableLogEntry> change) {
        if (change.wasAdded() && change.wasRemoved()) {
            throw new IllegalStateException("add and remove in one change is not supported ");
        }

        final List<Delta> deltas = new ArrayList<>();
        for (final ObservableLogEntry logEntry : change.getAddedSubList()) {
            deltas.add(addedLogEntry(logEntry));
        }
        for (final ObservableLogEntry logEntry : change.getRemoved()) {
            deltas.add(new RemoveLogEntryDelta());
        }
        sendOperation(new AddDeltasOperation(deltas));
    }


    private synchronized void onTileContentsChanged(final ListChangeListener.Change<? extends ObservableTileContent> change) {
        final List<Delta> deltas = new ArrayList<>();
        for (final ObservableTileContent addedContent : change.getAddedSubList()) {
            deltas.add(addedTileContent(addedContent));
        }
        for (final ObservableTileContent removedContent : change.getRemoved()) {
            deltas.add(new RemoveTileContentDelta(contentIdRelation.get(removedContent)));
            contentIdRelation.remove(removedContent);
        }
        sendOperation(new AddDeltasOperation(deltas));
    }

    private synchronized void onHamsterDirectionChanged(final ObservableHamster hamster, final Direction newDirection) {
        sendOperation(new AddDeltaOperation(new RotateHamsterDelta(contentIdRelation.get(hamster), newDirection)));
    }

    private synchronized void onHamsterAdded(final ListChangeListener.Change<? extends ObservableHamster> change) {
        final List<Delta> deltas = new ArrayList<>();
        for (final ObservableHamster hamster : change.getAddedSubList()) {
            deltas.add(new RotateHamsterDelta(contentIdRelation.get(hamster), hamster.getDirection()));
        }
        sendOperation(new AddDeltasOperation(deltas));
    }

    private synchronized Delta addedTileContent(final ObservableTileContent addedContent) {
        contentIdRelation.put(addedContent, idCounter);
        final Delta delta = new AddTileContentDelta(TileContentType.fromObservable(addedContent),
                addedContent.getCurrentLocation().orElseThrow(), idCounter);
        idCounter++;
        return delta;
    }

    private synchronized Delta addedLogEntry(final ObservableLogEntry logEntry) {
        return new AddLogEntryDelta(logEntry.getMessage(),
                Optional.ofNullable(logEntry.getHamster()).map(contentIdRelation::get));
    }
}
