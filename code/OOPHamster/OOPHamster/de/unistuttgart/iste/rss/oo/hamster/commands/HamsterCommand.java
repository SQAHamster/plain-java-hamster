package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;

public abstract class HamsterCommand extends Command {
    protected final HamsterManipulator hamsterManipulator;
    protected final Hamster hamster;

    public HamsterCommand(final HamsterManipulator manipulator) {
        super();
        this.hamsterManipulator = manipulator;
        this.hamster = manipulator.getHamster();
    }
}
