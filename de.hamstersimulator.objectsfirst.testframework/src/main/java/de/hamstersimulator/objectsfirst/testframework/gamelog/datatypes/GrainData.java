package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Location;

/**
 * Data representing a Grain TileContent
 */
public class GrainData extends TileContentData {

    public GrainData(final Location location) {
        super(TileContentType.GRAIN, location);
    }
}
