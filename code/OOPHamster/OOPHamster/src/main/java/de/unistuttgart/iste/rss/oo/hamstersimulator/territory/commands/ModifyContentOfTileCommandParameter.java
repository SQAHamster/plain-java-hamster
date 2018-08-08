package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

public final class ModifyContentOfTileCommandParameter {
    private final Location location;
    private final TileContent tileContent;

    public ModifyContentOfTileCommandParameter(final Location location, final TileContent tileContent) {
        this.location = location;
        this.tileContent = tileContent;
    }

    public Location getLocation() {
        return location;
    }

    public TileContent getTileContent() {
        return tileContent;
    }
}