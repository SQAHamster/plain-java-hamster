package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public abstract class HamsterCompositeBaseCommand extends CompositeBaseCommand {

    protected final PropertyMap<Hamster> hamsterState;
    protected final Hamster hamster;

    public HamsterCompositeBaseCommand(final PropertyMap<Hamster> hamsterState) {
        super();
        this.hamsterState = hamsterState;
        this.hamster = hamsterState.getPropertyOwner();
    }

}