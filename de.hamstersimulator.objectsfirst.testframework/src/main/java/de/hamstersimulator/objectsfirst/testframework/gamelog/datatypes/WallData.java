package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Location;

/**
 * Data representing a Wall TileContent
 */
public class WallData extends TileContentData {
    public WallData(final Location location) {
        super(TileContentType.WALL, location);
    }
}
