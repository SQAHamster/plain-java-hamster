package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public class GrainData extends TileContentData {

    public GrainData(final Location location) {
        super(TileContentType.GRAIN, location);
    }
}
