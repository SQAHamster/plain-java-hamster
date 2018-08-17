package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class Territory {

    final ReadOnlyObjectWrapper<Size> territorySize = new ReadOnlyObjectWrapper<Size>(this, "territorySize", new Size(0, 0));
    final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());

    private final Hamster defaultHamster;

    /**
     *
     * @param editStack
     * @param territoryFileName
     */
    public Territory() {
        super();

        this.defaultHamster = new Hamster();
    }

    public Size getSize() {
        return this.territorySize.get();
    }

    public ReadOnlyObjectProperty<Size> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    public Tile getTileAt(final Location location) {
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

        return tiles.get(getListIndexFromLocation(location));
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.territorySize.get().getColumnCount() &&
                newHamsterPosition.getRow() < this.territorySize.get().getRowCount();
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public TerritoryBuilder getTerritoryBuilder() {
        return new TerritoryBuilder(this);
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.territorySize.get().getColumnCount() + location.getColumn();
    }
}