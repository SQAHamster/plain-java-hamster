package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.SpecificationVisitor;

public class CompositeCommandSpecification implements CommandSpecification {

    private final AbstractCompositeCommand myParent;

    public CompositeCommandSpecification(final AbstractCompositeCommand myParent) {
        super();
        this.myParent = myParent;
    }

    public List<CommandSpecification> getSpecifications() {
        final List<CommandSpecification> result = new LinkedList<>();
        for (final Command command : this.myParent.getCommandsToExecute()) {
            final SpecifiedCommand<?> currentCommand = (SpecifiedCommand<?>) command;
            result .add(currentCommand.getSpecification());
        }
        return result;
    }


    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
