package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.HamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.ModifyContentOfTileCommandSpecification;

public interface SpecificationVisitor {

    default void vist(final PropertyCommandSpecification propertyCommandSpecification) {throw new UnsupportedOperationException();}
    default void vist(final HamsterCommandSpecification hamsterCommandSpecification) {throw new UnsupportedOperationException();}
    default void visit(final ModifyContentOfTileCommandSpecification modifyContentOfTileCommandParameter) {throw new UnsupportedOperationException();}
    default void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification) {throw new UnsupportedOperationException();}

}
