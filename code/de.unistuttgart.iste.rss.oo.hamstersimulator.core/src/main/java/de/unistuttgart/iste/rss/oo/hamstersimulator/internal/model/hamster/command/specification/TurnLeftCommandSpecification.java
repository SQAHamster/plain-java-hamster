package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class TurnLeftCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableTurnLeftCommandSpecification {

    public TurnLeftCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
