package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public class HamsterData extends TileContentData {
    private final int hamsterId;
    private final Direction direction;

    public HamsterData(final Location location, final int hamsterId, final Direction direction) {
        super(TileContentType.HAMSTER, location);
        this.hamsterId = hamsterId;
        this.direction = direction;
    }
}
