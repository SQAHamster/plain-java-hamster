package de.hamstersimulator.objectsfirst.adapter.observables;

import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Size;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.List;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkArgument;

/**
 * Interface to observe changes on the internal territory
 * Provides read-only access to all tiles of the territory, and therefore all
 * contents on the territory
 */
public interface ObservableTerritory {


    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Return the size of the current territory. From the size
     * you can retrieve the number of rows and columns.
     * @return A size object representing the size of the territory.
     *         Is never null.
     */
    Size getSize();

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Returns an unmodifiable list with all hamsters on the territory.
     * There is always at least one hamster on the territory (the default hamster)
     * @return an unmodifiable list with all hamsters on the territory
     */
    List<? extends ObservableHamster> getHamsters();

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
    ObservableHamster getDefaultHamster();

    /**
     * Getter for the territorySize property
     * Can be used to get the current size of the territory, which
     * provides the amount of rows and columns
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<Size> territorySizeProperty();

    /**
     * Getter for the tiles property
     * Can be used to observe changes on any tile of the territory
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableTile> tilesProperty();

    /**
     * Getter for the hamsters property
     * Can be used to observe changes on any hamster on the territory
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableHamster> hamstersProperty();

    /**
     * Getter for the defaultHamster property
     * The default hamster always exists, so the property is always set
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<? extends ObservableHamster> defaultHamsterProperty();

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
    ObservableTile getTileAt(final Location location);

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
    boolean isLocationInTerritory(final Location location);

}
