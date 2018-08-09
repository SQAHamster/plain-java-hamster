package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification.ActionKind;

public abstract class CompositeBaseCommand<T extends CommandSpecification> extends AbstractBaseCommand<T> implements CommandInterface<T> {

    private List<AbstractBaseCommand<?>> commandsToExecute = Lists.newLinkedList();
    protected CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();

    public CompositeBaseCommand(final T specification) {
        super(specification);
    }

    @Override
    public void execute() {
        buildBeforeFirstExecution(compositeCommandBuilder);
        commandsToExecute = ImmutableList.copyOf(compositeCommandBuilder.commandsToExecute);
        commandsToExecute.forEach(command -> command.execute());
    }

    @Override
    public void undo() {
        commandsToExecute.forEach(command -> command.undo());
    }

    protected class CompositeCommandBuilder {
        private final List<AbstractBaseCommand<?>> commandsToExecute = Lists.newLinkedList();

        public CompositeCommandBuilder add(final List<AbstractBaseCommand<?>> commands) {
            commandsToExecute.addAll(commands);
            return this;
        }

        public CompositeCommandBuilder add(final AbstractBaseCommand<?> ... commands ) {
            for (final AbstractBaseCommand<?> command : commands) {
                commandsToExecute.add(command);
            }
            return this;
        }

        public <G> CompositeCommandBuilder addPropertyUpdateCommand(final PropertyMap<G> entity,
                final String propertyName,
                final Object value,
                final ActionKind action) {
            commandsToExecute.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(entity, propertyName, value, action));
            return this;
        }
    }

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {};
}
