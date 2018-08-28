package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;

public class CompositeCommand extends Command {

    private List<Command> commandsToExecute = new LinkedList<>();
    private final CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();
    private boolean isBuilt = false;
    private Consumer<CompositeCommandBuilder> constructor = null;

    public CompositeCommand setConstructor(final Consumer<CompositeCommandBuilder> constructor) {
        this.constructor = constructor;
        return this;
    }
    
    @Override
    public void execute() {
        if (!isBuilt) {
            buildBeforeFirstExecution(compositeCommandBuilder);
            isBuilt = true;
        }
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
        
        public <G> void newSetPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.SET));
        }

        public <G> void newAddToPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.ADD));
        }

        public <G> void newRemoveFromPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.REMOVE));
        }
    }

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        if (constructor != null) {
            constructor.accept(builder);
        }
    }
}
