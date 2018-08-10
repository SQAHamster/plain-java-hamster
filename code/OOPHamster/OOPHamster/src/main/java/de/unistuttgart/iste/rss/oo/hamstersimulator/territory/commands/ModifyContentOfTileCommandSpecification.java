package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TileContent;

public final class ModifyContentOfTileCommandSpecification implements CommandSpecification  {
    private final Location location;
    private final TileContent tileContent;

    public ModifyContentOfTileCommandSpecification(final Location location, final TileContent tileContent) {
        this.location = location;
        this.tileContent = tileContent;
    }

    public Location getLocation() {
        return location;
    }

    public TileContent getTileContent() {
        return tileContent;
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }
}