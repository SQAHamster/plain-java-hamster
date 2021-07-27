package de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

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
