package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.type.DeltaType;

/**
 * Delta which represents a removed tile content
 */
public class RemoveTileContentDelta extends Delta {
    private static final long serialVersionUID = 1368035267950332555L;

    /**
     * the id of the tile content to remove
     */
    private final int tileContentId;

    /**
     * Creates a new RemoveTileContentDelta
     * @param tileContentId the id of the tile content to remove
     */
    public RemoveTileContentDelta(final int tileContentId) {
        super(DeltaType.REMOVE_TILE_CONTENT);
        this.tileContentId = tileContentId;
    }
}
