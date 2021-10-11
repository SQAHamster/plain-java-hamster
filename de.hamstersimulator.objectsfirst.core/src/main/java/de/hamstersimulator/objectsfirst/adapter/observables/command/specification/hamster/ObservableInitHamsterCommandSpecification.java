package de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTerritory;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableAbstractTerritoryTileCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Direction;

/**
 * Observable version of command specification used to initialize hamster
 */
public interface ObservableInitHamsterCommandSpecification extends ObservableAbstractHamsterCommandSpecification,
        ObservableAbstractTerritoryTileCommandSpecification {

    /**
     * Get the hamster's new direction
     * @return the hamster's new direction (!= null)
     */
    Direction getNewDirection();

    /**
     * Get the hamster's new grain count
     * @return the hamster's new grain count (greater than or equal to 0)
     */
    int getNewGrainCount();

    /**
     * Get the territory the hamster is initialized on
     * @return the territory the hamster is initialized on (!= null)
     */
    ObservableTerritory getTerritory();
}
