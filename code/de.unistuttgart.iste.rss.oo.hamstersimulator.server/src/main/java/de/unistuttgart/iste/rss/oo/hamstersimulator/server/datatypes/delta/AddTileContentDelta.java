package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.TileContentType;

public class AddTileContentDelta extends Delta {
    private static final long serialVersionUID = 4014642903948847027L;

    private final int contentId;
    private final Location location;
    final TileContentType contentType;

    public AddTileContentDelta(final TileContentType tileContentType, final Location location, final int tileContentId) {
        super(DeltaType.ADD_TILE_CONTENT);
        this.contentType = tileContentType;
        this.location = location;
        this.contentId = tileContentId;
    }
}
