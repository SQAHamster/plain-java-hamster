package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class AddWallToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements CommandSpecification {

    public AddWallToTileCommandSpecification(final Location location) {
        super(location);
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }
}
