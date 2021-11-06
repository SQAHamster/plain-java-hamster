package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Location;

/**
 * Base class for all data classes representing TileContents
 */
public class TileContentData {
    /**
     * The type of the TileContent
     * Necessary because of JSON serialization
     */
    private final TileContentType type;
    /**
     * The (initial) location of the TileContent
     */
    private final Location location;

    protected TileContentData(final TileContentType type, final Location location) {
        this.type = type;
        this.location = location;
    }
}
