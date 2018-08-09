package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecificationVisitor;

public class HamsterCommandSpecification implements CommandSpecification {

    public enum CommandKind {
        MOVE,
        PICK,
        PUT,
        TURN
    }

    private final CommandKind commandKind;

    public HamsterCommandSpecification(final CommandKind commandKind) {
        super();
        this.commandKind = commandKind;
    }

    public CommandKind getCommandKind() {
        return commandKind;
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.vist(this);
    }

}
