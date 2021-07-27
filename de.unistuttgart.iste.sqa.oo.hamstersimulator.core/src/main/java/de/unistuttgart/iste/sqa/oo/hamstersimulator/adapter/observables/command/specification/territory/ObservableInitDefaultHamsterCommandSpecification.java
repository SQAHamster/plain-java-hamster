package de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;

/**
 * observable version of command specification used to init the default hamster at a specific location and direction
 */
public interface ObservableInitDefaultHamsterCommandSpecification extends ObservableAbstractTerritoryTileCommandSpecification {

    /**
     * Get the hamster's new direction
     * @return the hamster's new direction (!= null)
     */
    Direction getDirection();

    /**
     * Get the hamster's new grain count
     * @return the hamster's new grain count (greater than or equal to 0)
     */
    int getGrainCount();

}
