package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;

public interface ObservableInitDefaultHamsterCommandSpecification extends ObservableAbstractTerritoryTileCommandSpecification {

    Direction getDirection();

    int getGrainCount();

}
