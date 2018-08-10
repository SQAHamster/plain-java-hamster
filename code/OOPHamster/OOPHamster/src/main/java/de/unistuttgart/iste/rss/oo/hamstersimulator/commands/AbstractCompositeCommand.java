package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;

public abstract class AbstractCompositeCommand extends Command {

    private List<Command> commandsToExecute = Lists.newLinkedList();
    protected CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();

    @Override
    public void execute() {
        buildBeforeFirstExecution(compositeCommandBuilder);
        commandsToExecute = ImmutableList.copyOf(compositeCommandBuilder.commandsToExecute);
        commandsToExecute.forEach(command -> command.execute());
    }

    @Override
    public void undo() {
        final List<Command> undoCommands = Lists.newArrayList(commandsToExecute);
        Collections.reverse(undoCommands);
        undoCommands.stream().forEach(command -> command.undo());
    }

    public class CompositeCommandBuilder {
        private final List<Command> commandsToExecute = Lists.newLinkedList();

        public CompositeCommandBuilder add(final List<Command> commands) {
            commandsToExecute.addAll(commands);
            return this;
        }

        public CompositeCommandBuilder add(final Command ... commands ) {
            for (final Command command : commands) {
                commandsToExecute.add(command);
            }
            return this;
        }

        public <G> CompositeCommandBuilder addPropertyUpdateCommand(
                final Property<Object> property,
                final String propertyName,
                final Object value,
                final ActionKind action) {
            commandsToExecute.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(property, value, action));
            return this;
        }
    }

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {};
}
