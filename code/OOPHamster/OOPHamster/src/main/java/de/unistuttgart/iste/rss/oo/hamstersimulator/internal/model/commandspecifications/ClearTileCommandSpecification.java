package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public final class ClearTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements CommandSpecification {

    public ClearTileCommandSpecification(final Location location) {
        super(location);
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }
}
