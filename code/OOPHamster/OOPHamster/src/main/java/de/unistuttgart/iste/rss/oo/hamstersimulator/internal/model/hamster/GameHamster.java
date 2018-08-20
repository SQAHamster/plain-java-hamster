package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.util.LambdaVisitor;

public class GameHamster extends EditorHamster {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameHamster() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitHamsterCommandSpecification.class).then(this::createInitHamsterCommand).
                on(PickGrainCommandSpecification.class).then(this::createPickGrainCommand).
                on(PutGrainCommandSpecification.class).then(this::createPutGrainCommand).
                on(TurnLeftCommandSpecification.class).then(this::createTurnLeftCommand);
    }

    private Command createInitHamsterCommand(final InitHamsterCommandSpecification specification) {
        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.newSetPropertyCommand(direction, specification.getNewDirection());
                IntStream.
                    range(0, specification.getNewGrainCount()).
                    forEach(i -> builder.newAddToPropertyCommand(grainInMouth, new Grain()));
            }
        };
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
    
    @Override
    public Optional<Command> getCommandFromSpecification(final CommandSpecification spec) {
        final Optional<Command> editorCommand = super.getCommandFromSpecification(spec);
        if (editorCommand.isPresent()) {
            return editorCommand;
        }
        return Optional.ofNullable(this.editCommandFactory.apply(spec));
    }
    
}
