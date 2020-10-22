package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public interface ObservableInitHamsterCommandSpecification extends ObservableAbstractHamsterCommandSpecification {

    Location getLocation();

    Direction getNewDirection();

    int getNewGrainCount();

    ObservableTerritory getTerritory();
}