package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class TurnLeftCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableTurnLeftCommandSpecification {

    public TurnLeftCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
