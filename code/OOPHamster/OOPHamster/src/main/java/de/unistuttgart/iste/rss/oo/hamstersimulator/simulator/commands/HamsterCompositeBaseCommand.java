package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public abstract class HamsterCompositeBaseCommand extends CompositeBaseCommand implements GameCommand {

    protected final Hamster hamster;

    public HamsterCompositeBaseCommand(final Hamster hamster) {
        super();
        this.hamster = hamster;
    }

}