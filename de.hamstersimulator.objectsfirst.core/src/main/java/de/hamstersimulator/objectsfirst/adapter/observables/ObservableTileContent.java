package de.hamstersimulator.objectsfirst.adapter.observables;

import de.hamstersimulator.objectsfirst.datatypes.Location;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Optional;

/**
 * Interface to observe changes on a internal tile content
 * Provides read-only access to the tile on which this content currently is
 */
public interface ObservableTileContent {

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     */
    /**
     * Get the current tileContent location.
     * @return The current hamster's location in the territory.
     */
    Optional<Location> getCurrentLocation();

    /**
     * Getter for the currentTile property
     * Provides access to the tile on which this content currently is.
     * A content must not always be on a tile, so this is optional
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<? extends Optional<? extends ObservableTile>> currentTileProperty();
}
