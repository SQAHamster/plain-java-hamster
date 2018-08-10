package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.InitializeTerritoryCommandSpecification;

public interface SpecificationVisitor {

    default void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final ClearTileCommandSpecification clearTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification) {throw new UnsupportedOperationException();}

}
