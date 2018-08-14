package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public final class InitializeTerritoryCommandSpecification implements CommandSpecification {

    private final Size size;

    public InitializeTerritoryCommandSpecification(final Size size) {
        super();
        this.size = size;
    }

    public Size getSize() {
        return size;
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
