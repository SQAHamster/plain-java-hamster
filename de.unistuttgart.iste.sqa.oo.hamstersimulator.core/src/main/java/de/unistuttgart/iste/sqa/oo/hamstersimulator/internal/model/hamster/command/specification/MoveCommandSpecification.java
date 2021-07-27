package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class MoveCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableMoveCommandSpecification {

    public MoveCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
