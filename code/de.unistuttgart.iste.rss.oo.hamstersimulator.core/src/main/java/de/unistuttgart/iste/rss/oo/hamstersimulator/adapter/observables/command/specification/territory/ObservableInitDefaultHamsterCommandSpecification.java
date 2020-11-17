package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;

/**
 * observable version of command specification used to init the default hamster at a specific location and direction
 */
public interface ObservableInitDefaultHamsterCommandSpecification extends ObservableAbstractTerritoryTileCommandSpecification {

    Direction getDirection();

    int getGrainCount();

}
