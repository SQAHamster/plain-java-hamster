package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class TurnLeftCommand extends HamsterCompositeBaseCommand {

    public TurnLeftCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Direction newDirection = this.hamster.getDirection().left();
        builder.add(new UnidirectionalUpdatePropertyCommand<Hamster>(this.hamsterState, new PropertyCommandSpecification("direction", newDirection, ActionKind.SET)));
    }

}
