package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.InitDefaultHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.util.LambdaVisitor;

public class EditorHamster extends ReadOnlyHamster {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public EditorHamster() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitDefaultHamsterCommandSpecification.class).then(this::createInitDefaultHamsterCommand);
    }
    
    private Command createInitDefaultHamsterCommand(final InitDefaultHamsterCommandSpecification specification) {
        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.newSetPropertyCommand(direction, specification.getDirection());
                IntStream.
                    range(0, specification.getGrainCount()).
                    forEach(i -> builder.newAddToPropertyCommand(grainInMouth, new Grain()));
            }
        };
    }

    public Optional<Command> getCommandFromSpecification(final CommandSpecification spec) {
        return Optional.ofNullable(this.editCommandFactory.apply(spec));
    }
    
}
