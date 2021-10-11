package de.hamstersimulator.objectsfirst.internal.model.hamster;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CompositeCommand;
import de.hamstersimulator.objectsfirst.exceptions.HamsterNotOnNonBlockingTileException;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.Grain;
import de.hamstersimulator.objectsfirst.utils.LambdaVisitor;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class GameHamster extends EditorHamster {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameHamster() {
        super();
        this.editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitHamsterCommandSpecification.class).then(this::createInitHamsterCommand).
                on(PickGrainCommandSpecification.class).then(this::createPickGrainCommand).
                on(PutGrainCommandSpecification.class).then(this::createPutGrainCommand).
                on(TurnLeftCommandSpecification.class).then(this::createTurnLeftCommand);
    }

    @Override
    public Optional<Command> getCommandFromSpecification(final CommandSpecification spec) {
        final Optional<Command> editorCommand = super.getCommandFromSpecification(spec);
        if (editorCommand.isPresent()) {
            return editorCommand;
        }
        return Optional.ofNullable(this.editCommandFactory.apply(spec));
    }

    private Command createInitHamsterCommand(final InitHamsterCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
            builder.newSetPropertyCommand(this.direction, specification.getNewDirection());
            IntStream.
                    range(0, specification.getNewGrainCount()).
                    forEach(i -> builder.newAddToPropertyCommand(this.grainInMouth, new Grain()));
        }).setPreconditionConstructor(builder -> {
            builder.addNewPrecondition(HamsterNotOnNonBlockingTileException::new,
                    () -> specification.getTerritory().getTileAt(specification.getLocation()).isBlocked());
        });
    }

    private Command createPickGrainCommand(final PickGrainCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
            builder.newAddToPropertyCommand(this.grainInMouth, specification.getGrain());
        });
    }

    private Command createPutGrainCommand(final PutGrainCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
            builder.newRemoveFromPropertyCommand(this.grainInMouth, specification.getGrain());
        });
    }

    private Command createTurnLeftCommand(final TurnLeftCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
            builder.newSetPropertyCommand(this.direction, this.getDirection().left());
        });
    }

}
