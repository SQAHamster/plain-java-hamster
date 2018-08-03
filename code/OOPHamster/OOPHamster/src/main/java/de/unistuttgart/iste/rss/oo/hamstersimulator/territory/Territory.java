package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TerritoryListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileRemovedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;

public class Territory {

    private final ReadOnlyObjectWrapper<Dimension> territorySize = new ReadOnlyObjectWrapper<Dimension>(this, "territorySize", new Dimension(0, 0));
    private final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());

    private final HamsterSimulator simulator;
    private final List<TerritoryListener> listeners = new LinkedList<>();
    private Hamster defaultHamster;

    /**
     *
     * @param territoryFileName
     */
    public Territory(final HamsterSimulator simulator) {
        super();
        this.setSize(new Dimension(0, 0));

        this.simulator = simulator;
        Hamster.hamsterSetProperty().addListener(new SetChangeListener<Hamster>() {

            @Override
            public void onChanged(final Change<? extends Hamster> change) {
                if (change.wasAdded()) {
                    final Hamster newHamster = change.getElementAdded();
                    newHamster.currentTileProperty().addListener((property, oldValue, newValue) -> {
                        oldValue.ifPresent(tile -> tile.removeObjectFromContent(newHamster));
                        newValue.ifPresent(tile -> tile.addObjectToContent(newHamster));
                    });
                    newHamster.getCurrentTile().ifPresent(t -> t.addObjectToContent(newHamster));
                }
            }
        });
    }

    public int getRowCount() {
        return this.territorySize.get().height;
    }

    public int getColumnCount() {
        return this.territorySize.get().width;
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

    public Territory setSize(final Dimension newSize) {
        assert newSize.width >= 0 && newSize.height >= 0;
        disposeAllExistingTiles();
        initNewTileStore(newSize);
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

    public ReadOnlyObjectProperty<Dimension> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
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

    private void initNewTileStore(final Dimension newSize) {
        this.territorySize.set(newSize);
        this.tiles.clear();
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
        return location.getRow() * this.getColumnCount() + location.getColumn();
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