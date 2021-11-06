package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;

public class HamsterData extends TileContentData {
    private final int hamsterId;
    private final Direction direction;

    public HamsterData(final Location location, final int hamsterId, final Direction direction) {
        super(TileContentType.HAMSTER, location);
        this.hamsterId = hamsterId;
        this.direction = direction;
    }
}
