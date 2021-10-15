package de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservablePickGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;
import de.hamstersimulator.objectsfirst.internal.model.territory.Grain;

public class PickGrainCommandSpecification extends AbstractHamsterCommandSpecification implements ObservablePickGrainCommandSpecification {

    private final Grain grain;

    public PickGrainCommandSpecification(final GameHamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    @Override
    public Grain getGrain() {
        return this.grain;
    }

}
