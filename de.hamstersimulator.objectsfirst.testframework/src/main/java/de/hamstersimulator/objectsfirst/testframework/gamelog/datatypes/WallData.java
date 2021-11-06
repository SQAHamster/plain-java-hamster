package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Location;

public class WallData extends TileContentData {

    public WallData(final Location location) {
        super(TileContentType.WALL, location);
    }
}
