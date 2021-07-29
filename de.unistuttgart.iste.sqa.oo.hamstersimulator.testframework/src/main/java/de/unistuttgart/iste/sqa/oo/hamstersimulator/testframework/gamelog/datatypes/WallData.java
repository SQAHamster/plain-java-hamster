package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public class WallData extends TileContentData {

    public WallData(final Location location) {
        super(TileContentType.WALL, location);
    }
}
