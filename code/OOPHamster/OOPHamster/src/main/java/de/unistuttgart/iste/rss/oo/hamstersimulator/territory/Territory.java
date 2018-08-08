package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Dimension;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.SetTerritorySizeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;

public class Territory {

    private final ReadOnlyObjectWrapper<Dimension> territorySize = new ReadOnlyObjectWrapper<Dimension>(this, "territorySize", new Dimension(0, 0));
    private final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());
    private final PropertyMap<Territory> territoryState = new PropertyMap<Territory>(this, territorySize, tiles);

    private final HamsterSimulator simulator;
    private Hamster defaultHamster;

    /**
     *
     * @param territoryFileName
     */
    public Territory(final HamsterSimulator simulator) {
        super();
        checkNotNull(simulator, "Simulator not allowed to be null.");

        this.simulator = simulator;
        this.setSize(new Dimension(0, 0));
        Hamster.hamsterSetProperty().addListener(new SetChangeListener<Hamster>() {

            @Override
            public void onChanged(final Change<? extends Hamster> change) {
                if (change.wasAdded()) {
                    final Hamster newHamster = change.getElementAdded();
                    newHamster.currentTileProperty().addListener((property, oldValue, newValue) -> {
                        oldValue.ifPresent(tile -> tile.getState().<TileContent> getSetProperty("content").remove(newHamster));
                        newValue.ifPresent(tile -> tile.getState().<TileContent> getSetProperty("content").add(newHamster));
                    });
                    newHamster.getCurrentTile().ifPresent(tile -> tile.getState().<TileContent> getSetProperty("content").add(newHamster));
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
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

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

    public void setSize(final Dimension newDimension) {
        checkArgument(newDimension.width >= 0 && newDimension.height >= 0, "New Territory dimensions need to be positive!");
        final SetTerritorySizeCommand command = new SetTerritorySizeCommand(this.territoryState, newDimension);
        this.simulator.getCommandStack().execute(command);
    }

    public ReadOnlyObjectProperty<Dimension> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.getColumnCount() + location.getColumn();
    }
}