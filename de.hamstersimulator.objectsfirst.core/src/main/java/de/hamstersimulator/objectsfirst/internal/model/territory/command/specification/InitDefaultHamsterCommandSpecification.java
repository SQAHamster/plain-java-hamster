package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableInitDefaultHamsterCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

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
