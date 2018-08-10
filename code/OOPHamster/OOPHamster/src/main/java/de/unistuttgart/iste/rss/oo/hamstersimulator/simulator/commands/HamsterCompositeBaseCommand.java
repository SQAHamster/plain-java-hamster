package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;

public abstract class HamsterCompositeBaseCommand<T extends CommandSpecification> extends CompositeBaseCommand<T> implements GameCommand<T> {

    protected final Hamster hamster;

    public HamsterCompositeBaseCommand(final Hamster hamster, final T spec) {
        super(spec);
        this.hamster = hamster;
    }

}