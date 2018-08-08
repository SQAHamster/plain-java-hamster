package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.GameHamster;

public abstract class HamsterCompositeBaseCommand extends CompositeBaseCommand implements GameCommand {

    protected final GameHamster hamster;

    public HamsterCompositeBaseCommand(final GameHamster hamster) {
        super();
        this.hamster = hamster;
    }

}