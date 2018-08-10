package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitializeTerritoryCommandSpecification;

public interface SpecificationVisitor {

    default void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final ClearTileCommandSpecification clearTileCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification) {throw new UnsupportedOperationException();}

}
