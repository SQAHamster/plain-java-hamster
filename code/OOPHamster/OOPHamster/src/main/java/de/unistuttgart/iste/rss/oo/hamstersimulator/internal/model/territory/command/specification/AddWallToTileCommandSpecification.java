package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.SpecificationVisitor;

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
