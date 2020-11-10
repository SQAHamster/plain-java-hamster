package de.unistuttgart.iste.rss.oo.hamstersimulator.server.observer;


import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.TileContentType;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static de.unistuttgart.iste.rss.utils.Preconditions.*;

/**
 * Observes changes on the provided territory and log, creates deltas which represent
 * the changes and notifies the provided DeltaListener about theses changes
 * Also transforms the initial state to a list of deltas
 */
public class TerritoryLogObserver extends Observer {

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
     * Lock used for synchronization
     */
    private final ReentrantLock lock = new ReentrantLock(true);

    /**
     * Creates a new TerritoryObserver which observes changes on the provided territory
     * and notifies the provided listener
     * @param territory the ObservableTerritory to observe changes on, must be != null
     * @param log the ObservableLog to observe changes on, must be != null
     * @param listener the listener to notify when changes occur, must be != null
     */
    public TerritoryLogObserver(final ObservableTerritory territory, final ObservableLog log, final DeltaListener listener) {
        super(listener);
        checkNotNull(territory);
        checkNotNull(log);

        initListeners(territory, log);
        initInitialState(territory, log);
    }

    /*@
     @ requires territory != null;
     @ requires log != null;
     @*/
    /**
     * Initializes all change listeners on the Territory and HamsterGameController
     * @param territory the ObservableTerritory on which all territory listeners should be added
     * @param log the ObservableLogon which all log listeners should be added
     */
    private void initListeners(final ObservableTerritory territory, final ObservableLog log) {
        checkNotNull(territory);
        checkNotNull(log);

        log.logProperty().addListener(this::onLogChanged);
        initTerritoryListeners(territory);
        initHamsterListeners(territory);
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
            notifyListener(List.of(new NewTerritoryDelta(newSize)));
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
     @ requires log != null;
     @*/
    /**
     * Sends an AddDeltasOperation to the server which contains all deltas which make
     * up the initial game state
     * @param territory the ObservableTerritory which should be serialized into deltas
     * @param log the ObservableLog which should be serialized into deltas
     */
    private void initInitialState(final ObservableTerritory territory, final ObservableLog log) {
        checkNotNull(territory);
        checkNotNull(log);

        final List<Delta> deltas = new ArrayList<>();
        initInitialTerritoryState(territory, deltas);
        initInitialLogState(log, deltas);
        notifyListener(deltas);
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
            notifyListener(deltas);
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
            notifyListener(deltas);
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

            notifyListener(List.of(new RotateHamsterDelta(contentIdRelation.get(hamster), newDirection)));
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
            notifyListener(deltas);
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

}
