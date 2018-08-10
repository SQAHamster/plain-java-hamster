package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;

public interface SpecificationVisitor {

    void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification);
    void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification);
    void visit(final ClearTileCommandSpecification clearTileCommandSpecification);
    void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification);
    void visit(final InitHamsterCommandSpecification initHamsterCommandSpecification);
    void visit(CompositeCommandSpecification compositeCommandSpecification);

}
