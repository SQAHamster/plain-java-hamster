package de.hamstersimulator.objectsfirst.internal.model.hamster;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.hamstersimulator.objectsfirst.commands.CompositeCommand;
import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.Grain;
import de.hamstersimulator.objectsfirst.internal.model.territory.command.specification.InitDefaultHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.utils.LambdaVisitor;

public class EditorHamster extends ReadOnlyHamster {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public EditorHamster() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitDefaultHamsterCommandSpecification.class).then(this::createInitDefaultHamsterCommand);
    }

    private Command createInitDefaultHamsterCommand(final InitDefaultHamsterCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
                builder.newSetPropertyCommand(direction, specification.getDirection());
                IntStream.
                    range(0, specification.getGrainCount()).
                    forEach(i -> builder.newAddToPropertyCommand(grainInMouth, new Grain()));
            });
    }

    public Optional<Command> getCommandFromSpecification(final CommandSpecification specification) {
        return Optional.ofNullable(this.editCommandFactory.apply(specification));
    }

}
