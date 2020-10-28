package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.TileContentType;

public class AddTileContentDelta extends Delta {
    private static final long serialVersionUID = 4014642903948847027L;

    private final int tileContentId;
    private final Location location;
    final TileContentType tileContentType;

    public AddTileContentDelta(final TileContentType tileContentType, final Location location, final int tileContentId) {
        super(DeltaType.ADD_TileContent);
        this.tileContentType = tileContentType;
        this.location = location;
        this.tileContentId = tileContentId;
    }
}
