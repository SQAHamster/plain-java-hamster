package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.awt.Dimension;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;

public class InitializeTerritoryCommandSpecification implements CommandSpecification {

    final Dimension dimension;

    public InitializeTerritoryCommandSpecification(final Dimension dimension) {
        super();
        this.dimension = dimension;
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }
}
