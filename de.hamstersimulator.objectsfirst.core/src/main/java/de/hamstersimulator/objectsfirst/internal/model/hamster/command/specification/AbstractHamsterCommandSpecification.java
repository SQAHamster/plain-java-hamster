package de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;

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
