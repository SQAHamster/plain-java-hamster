package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.SetDirectionCommand;

public class TurnLeftCommand extends HamsterCompositeBaseCommand {

    public TurnLeftCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Direction newDirection = this.hamster.getDirection().left();
        builder.add(new SetDirectionCommand(this.hamsterState, newDirection));
    }

}
