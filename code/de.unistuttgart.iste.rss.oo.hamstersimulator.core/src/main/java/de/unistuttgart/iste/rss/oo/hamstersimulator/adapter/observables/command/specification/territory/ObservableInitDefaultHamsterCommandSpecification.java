package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;

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
     * @return the hamster's new grain count (>= 0)
     */
    int getGrainCount();

}
