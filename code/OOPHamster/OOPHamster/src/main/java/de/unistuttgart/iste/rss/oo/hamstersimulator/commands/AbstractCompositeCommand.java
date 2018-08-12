package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;

public abstract class AbstractCompositeCommand extends Command {

    List<Command> commandsToExecute = new LinkedList<>();
    protected CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();

    @Override
    public void execute() {
        buildBeforeFirstExecution(compositeCommandBuilder);
        commandsToExecute = Collections.unmodifiableList(new ArrayList<>(compositeCommandBuilder.commandsToExecute));
        commandsToExecute.forEach(command -> command.execute());
    }

    @Override
    public void undo() {
        final List<Command> undoCommands = new ArrayList<>(commandsToExecute);
        Collections.reverse(undoCommands);
        undoCommands.stream().forEach(command -> command.undo());
    }

    public class CompositeCommandBuilder {
        private final List<Command> commandsToExecute = new LinkedList<>();

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

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {}

    public List<Command> getCommandsToExecute() {
        return Collections.unmodifiableList(this.commandsToExecute);
    };
}
