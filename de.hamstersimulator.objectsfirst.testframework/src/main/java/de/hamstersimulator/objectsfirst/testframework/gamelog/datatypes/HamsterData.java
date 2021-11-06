package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;

/**
 * Data representing a Hamster TileContent
 */
public class HamsterData extends TileContentData {
    /**
     * The unique id of the hamster
     */
    private final int hamsterId;
    /**
     * The initial direction of the Hamster
     */
    private final Direction direction;

    public HamsterData(final Location location, final int hamsterId, final Direction direction) {
        super(TileContentType.HAMSTER, location);
        this.hamsterId = hamsterId;
        this.direction = direction;
    }
}
