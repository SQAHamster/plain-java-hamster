package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;

public abstract class AbstractHamsterCompositeBaseCommand extends AbstractCompositeCommand {

    protected final Hamster hamster;

    public AbstractHamsterCompositeBaseCommand(final Hamster hamster) {
        super();
        this.hamster = hamster;
    }

}