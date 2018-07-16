package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.Territory;

public abstract class HamsterCommand extends Command {
    protected final HamsterManipulator hamsterManipulator;
    protected final Hamster hamster;
    protected final Territory territory;

    public HamsterCommand(final HamsterManipulator manipulator, final Territory territory) {
        super();
        this.hamsterManipulator = manipulator;
        this.hamster = manipulator.getHamster();
        this.territory = territory;
    }
}
