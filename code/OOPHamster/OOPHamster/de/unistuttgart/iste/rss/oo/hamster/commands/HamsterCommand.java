package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Territory;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterManipulator;

public abstract class HamsterCommand extends Command {
    protected final HamsterManipulator hamsterManipulator;
    protected final Hamster hamster;
    protected final Territory territory;

    public HamsterCommand(final HamsterManipulator manipulator) {
        super();
        this.hamsterManipulator = manipulator;
        this.hamster = manipulator.getHamster();
        this.territory = manipulator.getTerritory();
    }
}
