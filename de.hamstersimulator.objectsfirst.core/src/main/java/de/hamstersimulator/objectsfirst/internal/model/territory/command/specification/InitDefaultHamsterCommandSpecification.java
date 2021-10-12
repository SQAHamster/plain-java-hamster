package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableInitDefaultHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;

public class InitDefaultHamsterCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableInitDefaultHamsterCommandSpecification {

    private final Direction direction;
    private final int grainCount;

    public InitDefaultHamsterCommandSpecification(final Location location, final Direction direction, final int grainCount) {
        super(location);
        this.direction = direction;
        this.grainCount = grainCount;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public int getGrainCount() {
        return this.grainCount;
    }

}
