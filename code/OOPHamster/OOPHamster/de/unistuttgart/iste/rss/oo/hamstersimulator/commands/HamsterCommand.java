package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

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
