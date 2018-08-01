package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

abstract class HamsterCommand extends Command {
    protected final HamsterStateChanger stateChanger;
    protected final Hamster hamster;

    public HamsterCommand(final HamsterStateChanger stateChanger) {
        super();
        this.stateChanger = stateChanger;
        this.hamster = stateChanger.getHamster();
    }

    protected Territory getTerritory() {
        assert this.hamster.getCurrentTile().isPresent();

        return this.hamster.getCurrentTerritory();
    }
}
