package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class ReadOnlyTerritory {

    final ReadOnlyObjectWrapper<Size> territorySize = new ReadOnlyObjectWrapper<Size>(this, "territorySize", new Size(0, 0));
    final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());
    final ReadOnlyObjectWrapper<Hamster> defaultHamster = new ReadOnlyObjectWrapper<Hamster>(this, "defaultHamster", new Hamster());

    public Size getSize() {
        return this.territorySize.get();
    }

    public ReadOnlyObjectProperty<Size> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster.get();
    }

    public ReadOnlyObjectProperty<Hamster> defaultHamsterProperty() {
        return this.defaultHamster.getReadOnlyProperty();
    }

    public Tile getTileAt(final Location location) {
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

        return tiles.get(getListIndexFromLocation(location));
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.territorySize.get().getColumnCount() &&
                newHamsterPosition.getRow() < this.territorySize.get().getRowCount();
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.territorySize.get().getColumnCount() + location.getColumn();
    }
}