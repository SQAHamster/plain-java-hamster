package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public abstract class CompositeBaseCommand extends Command {

    private List<Command> commandsToExecute = Lists.newLinkedList();
    protected CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();

    @Override
    protected void execute() {
        buildBeforeFirstExecution(compositeCommandBuilder);
        commandsToExecute = ImmutableList.copyOf(compositeCommandBuilder.commandsToExecute);
        commandsToExecute.forEach(command -> command.execute());
    }

    @Override
    protected void undo() {
        commandsToExecute.forEach(command -> command.undo());
    }

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {};

    protected class CompositeCommandBuilder {
        private final List<Command> commandsToExecute = Lists.newLinkedList();

        public CompositeCommandBuilder add(final Command ... commands ) {
            for (final Command command : commands) {
                commandsToExecute.add(command);
            }
            return this;
        }
    }
}
