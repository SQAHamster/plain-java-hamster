package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.SpecificationVisitor;

public interface CommandSpecification extends Cloneable {
    void visit(SpecificationVisitor visitor);
    public Object clone() throws CloneNotSupportedException;
}
