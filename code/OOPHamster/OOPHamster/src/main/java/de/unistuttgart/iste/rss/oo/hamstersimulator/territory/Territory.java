package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class Territory {

    private final HamsterSimulator simulator;
    private final List<TerritoryListener> listeners = new LinkedList<>();
    private Tile[][] tiles;
    private Hamster defaultHamster;

    /**
     *
     * @param territoryFileName
     */
    public Territory(final HamsterSimulator simulator) {
        super();
        this.simulator = simulator;
        this.setSize(0, 0);
    }

    public int getRowCount() {
        return this.tiles.length;
    }

    public int getColumnCount() {
        return this.tiles.length > 0 ? this.tiles[0].length : 0;
    }

    public Tile getTileAt(final Location location) {
        return tiles[location.getRow()][location.getColumn()];
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.getColumnCount() &&
                newHamsterPosition.getRow() < this.getRowCount();
    }

    public void loadTerritoryFromFile(final String territoryFile) {
        TerritoryLoader.loader(this).loadFromFile(territoryFile);
    }

    public Territory setSize(final int columnCount, final int rowCount) {
        if (this.tiles != null) {
            for (int row = 0; row < this.tiles.length; row++) {
                for (int column = 0; column < this.tiles[0].length; column++) {
                    notifyTileRemoved(new TileRemovedEvent(this, this.tiles[row][column]));
                    this.tiles[row][column] = null;
                }
            }
            this.tiles = null;
        }
        this.tiles = new Tile[rowCount][columnCount];
        notifyResized(new TerritoryResizedEvent(this, getColumnCount(), getRowCount()));
        for (int row = 0; row < this.tiles.length; row++) {
            for (int column = 0; column < this.tiles[0].length; column++) {
                this.tiles[row][column] = Tile.createEmptyTile(this, new Location(row, column));
                notifyTileCreated(new TileAddedEvent(this, this.tiles[row][column]));
            }
        }
        return this;
    }

    public Territory wallAt(final int row, final int column) {
        this.getTileAt(new Location(row, column)).addObjectToContent(new Wall());
        return this;
    }

    public Territory defaultHamsterAt(final int row, final int column, final Direction direction, final int grainCount) {
        if (this.defaultHamster != null) {
            this.getTileAt(defaultHamster.getCurrentPosition().get()).removeObjectFromContent(this.defaultHamster);
            // TODO: hamster.dispose!
            this.defaultHamster = null;
        }
        this.defaultHamster = new Hamster(
                simulator,
                new Location(row, column),
                direction,
                grainCount);
        this.getTileAt(new Location(row, column)).addObjectToContent(this.defaultHamster);
        return this;
    }

    public void addTerritoryListener(final TerritoryListener listener) {
        this.listeners.add(listener);
    }

    public void removeTerritoryListener(final TerritoryListener listener) {
        this.listeners.remove(listener);
    }

    public Territory grainAt(final int row, final int column) {
        return this.grainAt(row,column,1);
    }

    public Territory grainAt(final int row, final int column, final int grainCount) {
        this.putNewGrain(this.getTileAt(new Location(row, column)), grainCount);
        return this;
    }

    private void putNewGrain(final Tile tile, final int count) {
        for (int i = 0; i < count; i++) {
            tile.addObjectToContent(new Grain());
        }
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