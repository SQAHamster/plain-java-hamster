package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableAbstractTerritoryTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

/**
 * Observable version of command specification used to initialize hamster
 */
public interface ObservableInitHamsterCommandSpecification extends ObservableAbstractHamsterCommandSpecification, ObservableAbstractTerritoryTileCommandSpecification {

    /**
     * Get the hamster's new direction
     * @return the hamster's new direction (!= null)
     */
    Direction getNewDirection();

    /**
     * Get the hamster's new grain count
     * @return the hamster's new grain count (>= 0)
     */
    int getNewGrainCount();

    /**
     * Get the territory the hamster is initialized on
     * @return the territory the hamster is initialized on (!= null)
     */
    ObservableTerritory getTerritory();
}