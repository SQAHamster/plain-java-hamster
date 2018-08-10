package de.unistuttgart.iste.rss.oo.hamstersimulator.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;

public class TurnLeftCommand extends AbstractHamsterCompositeBaseCommand {

    public TurnLeftCommand(final Hamster hamster) {
        super(hamster);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Direction newDirection = this.hamster.getDirection().left();
        builder.add(this.hamster.getSetDirectionCommand(newDirection));
    }

}
