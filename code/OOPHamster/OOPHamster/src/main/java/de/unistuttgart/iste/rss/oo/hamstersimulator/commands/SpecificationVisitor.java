package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.InitializeTerritoryCommandSpecification;

public interface SpecificationVisitor {

    void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification);
    void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification);
    void visit(final ClearTileCommandSpecification clearTileCommandSpecification);
    void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification);
    void visit(final InitHamsterCommandSpecification initHamsterCommandSpecification);
    void visit(CompositeCommandSpecification compositeCommandSpecification);

}
