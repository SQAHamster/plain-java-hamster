package de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Location;

/**
 * Base interface for all ObservableCommandSpecifications that need a location, like
 * ObservableAddGrainsToTileCommandSpecification used to add grains to a tile
 * or ObservableAddWallToTileCommandSpecification which adds a wall to a tile
 */
public interface ObservableAbstractTerritoryTileCommandSpecification extends ObservableCommandSpecification {

    /**
     * Get the location associated with the command
     * @return the location, != null
     */
    Location getLocation();

}
