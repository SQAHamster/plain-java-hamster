package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public final class AddWallToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements CommandSpecification {

    public AddWallToTileCommandSpecification(final Location location) {
        super(location);
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