package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public abstract class AbstractHamsterCommandSpecification implements CommandSpecification, ObservableAbstractHamsterCommandSpecification {
    private final GameHamster hamster;

    public AbstractHamsterCommandSpecification(final GameHamster hamster) {
        super();
        this.hamster = hamster;
    }

    @Override
    public GameHamster getHamster() {
        return this.hamster;
    }

}
