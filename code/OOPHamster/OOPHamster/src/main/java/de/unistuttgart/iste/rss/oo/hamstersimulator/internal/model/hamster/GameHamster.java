package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import java.util.function.Function;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.util.LambdaVisitor;

public class GameHamster extends EditorHamster {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameHamster() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(MoveCommandSpecification.class).then(this::createMoveCommand).
                on(PickGrainCommandSpecification.class).then(this::createPickGrainCommand).
                on(PutGrainCommandSpecification.class).then(this::createPutGrainCommand).
                on(TurnLeftCommandSpecification.class).then(this::createTurnLeftCommand);
    }

    private Command createPickGrainCommand(final PickGrainCommandSpecification specification) {
        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.newAddToPropertyCommand(grainInMouth, specification.getGrain());
            }
        };
    }

    private Command createPutGrainCommand(final PutGrainCommandSpecification specification) {
        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.newRemoveFromPropertyCommand(grainInMouth, specification.getGrain());
            }
        };
    }

    private Command createTurnLeftCommand(final TurnLeftCommandSpecification specification) {
        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Direction newDirection = getDirection().left();
                builder.newSetPropertyCommand(direction, newDirection);
            }
        };
    }

    private Command createMoveCommand(final MoveCommandSpecification specification) {
        return Command.EMPTY;
    }
    
    @Override
    public Command getCommandFromSpecification(final CommandSpecification spec) {
        return this.editCommandFactory.apply(spec);
    }
    
}
