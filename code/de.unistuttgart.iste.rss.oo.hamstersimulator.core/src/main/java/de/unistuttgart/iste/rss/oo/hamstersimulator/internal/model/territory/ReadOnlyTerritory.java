package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;

import java.util.Collections;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class ReadOnlyTerritory implements ObservableTerritory {

    final ReadOnlyObjectWrapper<Size> territorySize = new ReadOnlyObjectWrapper<>(this, "territorySize", new Size(0, 0));
    final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<>(this, "tiles", FXCollections.observableArrayList());
    final ReadOnlyObjectWrapper<ReadOnlyHamster> defaultHamster = new ReadOnlyObjectWrapper<>(this, "defaultHamster", initDefaultHamster());
    final ReadOnlyListWrapper<ReadOnlyHamster> hamsters = new ReadOnlyListWrapper<>(this, "hamsters", FXCollections.observableArrayList(defaultHamster.get()));

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     */
    /**
     * Return the size of the current territory. From the size
     * you can retrieve the number of rows and columns.
     * @return A size object representing the size of the territory.
     *         Is never null.
     */
    @Override
    public Size getSize() {
        return this.territorySize.get();
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     */
    /**
     * Returns an unmodifiable list with all hamsters on the territory.
     * There is always at least one hamster on the territory (the default hamster)
     * @return an unmodifiable list with all hamsters on the territory
     */
    @Override
    public List<ReadOnlyHamster> getHamsters() {
        return Collections.unmodifiableList(this.hamsters.get());
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Gets the default hamster of this territory. A default hamster always
     * exists on a territory, so this method never returns null.
     * @return The hamster object representing the default hamster of the
     *         territory.
     */
    @Override
    public ReadOnlyHamster getDefaultHamster() {
        return this.defaultHamster.get();
    }

    /**
     * Getter for the territorySize property
     * Can be used to get the current size of the territory, which
     * provides the amount of rows and columns
     * @return the property (not null)
     */
    @Override
    public ReadOnlyObjectProperty<Size> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    /**
     * Getter for the tiles property
     * Can be used to observe changes on any tile of the territory
     * @return the property (not null)
     */
    @Override
    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    /**
     * Getter for the hamsters property
     * Can be used to observe changes on any hamster on the territory
     * @return the property (not null)
     */
    @Override
    public ReadOnlyListProperty<ReadOnlyHamster> hamstersProperty() {
        return this.hamsters.getReadOnlyProperty();
    }

    /**
     * Getter for the defaultHamster property
     * The default hamster always exists, so the property is always set
     * @return the property (not null)
     */
    @Override
    public ReadOnlyObjectProperty<ReadOnlyHamster> defaultHamsterProperty() {
        return this.defaultHamster.getReadOnlyProperty();
    }

    /*@
     @ pure;
     @ requires location != null;
     @ requires isLocationInTerritory(location);
     @ ensures \result != null;
     @ ensures \result.getLocation().equals(location);
     @*/
    /**
     * Returns the Tile associated with the location
     * @param location a location in the territory (must be != null)
     * @return the Tile which is associated with the provided location
     * @throws IllegalArgumentException if location is null or not in the territory
     */
    @Override
    public Tile getTileAt(final Location location) {
        checkArgument(location != null, "Location must be != null");
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

        return tiles.get(getListIndexFromLocation(location));
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result <==> (location.getRow() < getSize().getRowCount()
                                && location.getColumn() < getSize().getColumnCount());
     @*/
    /**
     * For a given location, tests whether the location is inside the bound of the territory.
     * @param location The location to test for.
     * @return true if the location is inside the territory.
     */
    @Override
    public boolean isLocationInTerritory(final Location location) {
        return location.getColumn() < this.territorySize.get().getColumnCount() &&
                location.getRow() < this.territorySize.get().getRowCount();
    }

    protected ReadOnlyHamster initDefaultHamster() {
        return new ReadOnlyHamster();
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.territorySize.get().getColumnCount() + location.getColumn();
    }
}