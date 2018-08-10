package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitializeTerritoryCommandSpecification;

public interface SpecificationVisitor {

    void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification);
    void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification);
    void visit(final ClearTileCommandSpecification clearTileCommandSpecification);
    void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification);
    void visit(final InitHamsterCommandSpecification initHamsterCommandSpecification);
    void visit(CompositeCommandSpecification compositeCommandSpecification);

}
