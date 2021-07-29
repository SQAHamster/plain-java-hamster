package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public class TileContentData {
    private final TileContentType type;
    private final Location location;

    protected TileContentData(final TileContentType type, final Location location) {
        this.type = type;
        this.location = location;
    }
}
