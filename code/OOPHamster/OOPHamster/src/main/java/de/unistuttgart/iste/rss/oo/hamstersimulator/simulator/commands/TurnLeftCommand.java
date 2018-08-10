package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;

public class TurnLeftCommand extends HamsterCompositeBaseCommand<CompositeCommandSpecification> {

    public TurnLeftCommand(final Hamster hamster) {
        super(hamster, new CompositeCommandSpecification());
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Direction newDirection = this.hamster.getDirection().left();
        builder.add(this.hamster.getSetDirectionCommand(newDirection));
    }

}
