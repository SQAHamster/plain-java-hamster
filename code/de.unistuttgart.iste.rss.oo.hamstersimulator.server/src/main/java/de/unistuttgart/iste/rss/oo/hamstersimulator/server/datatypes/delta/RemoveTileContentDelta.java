package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

public class RemoveTileContentDelta extends Delta {
    private static final long serialVersionUID = 1368035267950332555L;

    private final int tileContentId;

    public RemoveTileContentDelta(final int tileContentId) {
        super(DeltaType.REMOVE_TILE_CONTENT);
        this.tileContentId = tileContentId;
    }
}
