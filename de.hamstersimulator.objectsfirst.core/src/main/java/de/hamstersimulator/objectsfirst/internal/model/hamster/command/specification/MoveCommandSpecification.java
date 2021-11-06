package de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;

public class MoveCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableMoveCommandSpecification {

    public MoveCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
