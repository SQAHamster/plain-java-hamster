package de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;

public class TurnLeftCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableTurnLeftCommandSpecification {

    public TurnLeftCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
