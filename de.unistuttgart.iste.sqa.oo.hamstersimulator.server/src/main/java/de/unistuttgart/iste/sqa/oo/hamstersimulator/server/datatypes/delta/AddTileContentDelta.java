package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.type.DeltaType;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.type.TileContentType;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

/**
 * Delta which represents an added tile content
 */
public class AddTileContentDelta extends Delta {
    private static final long serialVersionUID = 4014642903948847027L;

    /**
     * id of the added tile content
     */
    private final int tileContentId;
    /**
     * location where the tile content should be added
     */
    private final Location location;
    /**
     * The type of the tile content to add
     */
    private final TileContentType contentType;

    /**
     * Creates a new AddTileContentDelta
     * @param tileContentType the type of the added tile content, must be != null
     * @param location the location where to add the tile content, must be != null, should be a valid
     *                 location in the current territory
     * @param tileContentId the id for this added tile content
     */
    public AddTileContentDelta(final TileContentType tileContentType, final Location location, final int tileContentId) {
        super(DeltaType.ADD_TILE_CONTENT);
        checkNotNull(tileContentType);
        checkNotNull(location);

        this.contentType = tileContentType;
        this.location = location;
        this.tileContentId = tileContentId;
    }
}
