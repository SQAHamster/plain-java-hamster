package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications;

import java.awt.Dimension;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;

public final class InitializeTerritoryCommandSpecification implements CommandSpecification {

    private final Dimension dimension;

    public InitializeTerritoryCommandSpecification(final Dimension dimension) {
        super();
        this.dimension = dimension;
    }

    public Dimension getDimension() {
        return dimension;
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
