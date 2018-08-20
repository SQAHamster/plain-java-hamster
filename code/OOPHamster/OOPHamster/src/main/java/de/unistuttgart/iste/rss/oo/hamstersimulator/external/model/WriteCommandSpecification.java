package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.SpecificationVisitor;

public class WriteCommandSpecification implements CommandSpecification {

    private final String message;
    private final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster sender;

    public de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster getSender() {
        return sender;
    }

    public WriteCommandSpecification(final String message, final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster internalHamster) {
        super();
        this.message = message;
        this.sender = internalHamster;
    }

    public String getMessage() {
        return message;
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
