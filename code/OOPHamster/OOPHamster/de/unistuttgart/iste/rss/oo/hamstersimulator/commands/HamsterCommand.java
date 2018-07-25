package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

abstract class HamsterCommand extends Command {
    protected final HamsterStateChanger stateChanger;
    protected final Hamster hamster;
    protected final Territory territory;

    public HamsterCommand(final Territory territory, final HamsterStateChanger stateChanger) {
        super();
        this.stateChanger = stateChanger;
        this.hamster = stateChanger.getHamster();
        this.territory = territory;
    }
}
