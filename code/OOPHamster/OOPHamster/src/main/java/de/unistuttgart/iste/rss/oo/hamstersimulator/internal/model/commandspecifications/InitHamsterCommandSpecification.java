package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public final class InitHamsterCommandSpecification {

    private final Optional<Location> location;
    private final Direction newDirection;
    private final int newGrainCount;

    public InitHamsterCommandSpecification(final Optional<Location> location, final Direction newDirection, final int newGrainCount) {
        this.location = location;
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public Direction getNewDirection() {
        return newDirection;
    }

    public int getNewGrainCount() {
        return newGrainCount;
    }

}