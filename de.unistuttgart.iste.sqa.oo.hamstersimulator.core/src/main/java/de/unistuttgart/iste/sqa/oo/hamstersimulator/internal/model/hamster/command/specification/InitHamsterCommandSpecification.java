package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableInitHamsterCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;

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
