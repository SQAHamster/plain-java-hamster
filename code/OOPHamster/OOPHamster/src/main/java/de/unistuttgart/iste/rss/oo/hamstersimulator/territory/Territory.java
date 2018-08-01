package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterMovedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterStateListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TerritoryListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TerritoryResizedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileRemovedEvent;

public class Territory {

    private final HamsterSimulator simulator;
    private final List<TerritoryListener> listeners = new LinkedList<>();
    private final ArrayList<Tile> tiles = new ArrayList<>();
    private Hamster defaultHamster;
    private int rowCount;
    private int columnCount;

    /**
     *
     * @param territoryFileName
     */
    public Territory(final HamsterSimulator simulator) {
        super();
        this.setSize(0, 0);
        this.simulator = simulator;
        Hamster.addHamsterCreatedListener(createdEvent -> {
            final Hamster newHamster = createdEvent.getHamster();
            newHamster.addHamsterStateListener(new HamsterStateListener() {

                @Override
                public void onStateChanged(final HamsterStateChangedEvent e) {
                    if (e instanceof HamsterMovedEvent) {
                        final HamsterMovedEvent moveEvent = (HamsterMovedEvent)e;
                        moveEvent.getOldTile().ifPresent(tile -> tile.removeObjectFromContent(e.getHamster()));
                        moveEvent.getNewTile().ifPresent(tile -> tile.addObjectToContent(moveEvent.getHamster()));
                    }
                }
            });
        });
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public Tile getTileAt(final Location location) {
        assert isLocationInTerritory(location);

        return tiles.get(getListIndexFromLocation(location));
    }

    public Hamster getDefaultHamster() {
        if (this.defaultHamster == null) {
            this.defaultHamster = new Hamster(simulator);
        }
        return this.defaultHamster;
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.getColumnCount() &&
                newHamsterPosition.getRow() < this.getRowCount();
    }

    public TerritoryBuilder getTerritoryBuilder() {
        return new TerritoryBuilder(this.simulator, this);
    }

    public void loadTerritoryFromFile(final String territoryFile) {
        TerritoryLoader.loader(this).loadFromFile(territoryFile);
    }

    public Territory setSize(final int newColumnCount, final int newRowCount) {
        assert newColumnCount >= 0 && newRowCount >= 0;
        disposeAllExistingTiles();
        initNewTileStore(newColumnCount, newRowCount);
        notifyResized(new TerritoryResizedEvent(this, getColumnCount(), getRowCount()));
        createNewTiles();
        // TODO: Throw new Territory Reset Event to allow Undo at most until this point!
        return this;
    }

    public void addTerritoryListener(final TerritoryListener listener) {
        this.listeners.add(listener);
        forAllTilesDo(t -> {
            t.addTileListener(listener);
        });
    }

    public void removeTerritoryListener(final TerritoryListener listener) {
        this.listeners.remove(listener);
        forAllTilesDo(t -> {
            t.removeTileListener(listener);
        });
    }

    private void disposeAllExistingTiles() {
        if (this.tiles != null) {
            forAllTilesDo(t -> {
                t.dispose();
                for (final TileListener listener : this.listeners) {
                    t.removeTileListener(listener);
                }
                this.tiles.remove(t);
                notifyTileRemoved(new TileRemovedEvent(this, t));
            });
        }
    }

    private void initNewTileStore(final int newColumnCount, final int newRowCount) {
        this.columnCount = newColumnCount;
        this.rowCount = newRowCount;
        this.tiles.clear();
        this.tiles.ensureCapacity(getRowCount() * getColumnCount());
    }

    private void createNewTiles() {
        for (int row = 0; row < this.getRowCount(); row++) {
            for (int column = 0; column < this.getColumnCount(); column++) {
                final Tile newTile = Tile.createEmptyTile(this, Location.from(row, column));
                setTile(newTile);
                notifyTileCreated(new TileAddedEvent(this, newTile));
                for (final TileListener listener : this.listeners) {
                    newTile.addTileListener(listener);
                }
            }
        }
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * columnCount + location.getColumn();
    }

    private void forAllTilesDo(final Consumer<Tile> operation) {
        for (int row = 0; row < this.getRowCount(); row++) {
            for (int column = 0; column < this.getColumnCount(); column++) {
                operation.accept(this.getTileAt(Location.from(row,column)));
            }
        }
    }

    private void setTile(final Tile newTile) {
        tiles.add(getListIndexFromLocation(newTile.getLocation()), newTile);
    }

    private void notifyResized(final TerritoryResizedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.territoryResized(e);
        }
    }

    private void notifyTileCreated(final TileAddedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.tileAdded(e);
        }
    }

    private void notifyTileRemoved(final TileRemovedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.tileRemoved(e);
        }
    }
}