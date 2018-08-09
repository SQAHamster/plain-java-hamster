package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

public interface CommandSpecification extends Cloneable {
    void visit(SpecificationVisitor visitor);
}
