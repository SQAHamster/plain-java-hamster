package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Location;

public class TileContentData {
    private final TileContentType type;
    private final Location location;

    protected TileContentData(final TileContentType type, final Location location) {
        this.type = type;
        this.location = location;
    }
}
