package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public interface CommandSpecification extends Cloneable {
    void visit(SpecificationVisitor visitor);
    public Object clone() throws CloneNotSupportedException;
}
