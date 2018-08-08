package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.GameHamster;

public class TurnLeftCommand extends HamsterCompositeBaseCommand {

    public TurnLeftCommand(final GameHamster hamster) {
        super(hamster);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Direction newDirection = this.hamster.getDirection().left();
        builder.add(this.hamster.getSetDirectionCommand(newDirection));
    }

}
