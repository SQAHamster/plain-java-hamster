package de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableInitHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;
import de.hamstersimulator.objectsfirst.internal.model.territory.ReadOnlyTerritory;

public final class InitHamsterCommandSpecification extends AbstractHamsterCommandSpecification
        implements ObservableInitHamsterCommandSpecification {

    private final ReadOnlyTerritory territory;
    private final Location location;
    private final Direction newDirection;
    private final int newGrainCount;

    public InitHamsterCommandSpecification(final GameHamster hamster, final ReadOnlyTerritory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        super(hamster);
        this.territory = territory;
        this.location = location;
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Direction getNewDirection() {
        return this.newDirection;
    }

    @Override
    public int getNewGrainCount() {
        return this.newGrainCount;
    }

    @Override
    public ReadOnlyTerritory getTerritory() {
        return this.territory;
    }
}
