package de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes;

/**
 * Enumeration of the directions in which a hamster can look in the
 * hamster simulator. Its values are the regular directions, north, east,
 * south, and west
 *
 * @author Steffen Becker
 *
 */
public enum Direction {

    /**
     * North direction, i.e., towards the top of the screen
     */
    NORTH (new LocationVector(-1,0)),
    /**
     * South direction, i.e., towards the bottom of the screen
     */
    SOUTH (new LocationVector(1,0)),
    /**
     * West direction, i.e., towards the left of the screen
     */
    WEST (new LocationVector(0,-1)),
    /**
     * East direction, i.e., towards the right of the screen
     */
    EAST (new LocationVector(0,1));

    /**
     * A movement vector which represents a unit movement step
     * towards the direction instance of this direction object
     */
    private final LocationVector movementVector;

    /** Create a new direction object and associate its
     * unit location vector to it
     * @param locationVector A location vector which represents
     *        a single movement step (i.e., one tile) towards
     *        the direction represented by this direction object.
     */
    private Direction(final LocationVector locationVector) {
        this.movementVector = locationVector;
    }

    /**
     * @return Returns a location vector which represents
     *        a single movement step (i.e., one tile) towards
     *        the direction represented by this direction object.
     */
    public /*@ pure; helper @*/ LocationVector getMovementVector() {
        return movementVector;
    }

    /**
     * @return Returns a new, non-null direction object
     *         which is the direction of turning the hamster one step
     *         towards the left, i.e., counterclockwise.
     */
    public /*@ pure; helper @*/ Direction left() {
        switch (this) {
        case EAST:
            return Direction.NORTH;
        case NORTH:
            return Direction.WEST;
        case SOUTH:
            return Direction.EAST;
        case WEST:
            return Direction.SOUTH;
        }
        return null;
    }

    /**
     * @return Returns a new, non-null direction object
     *         which is the direction of turning the hamster one step
     *         towards the right, i.e., clockwise.
     */
    public /*@ pure; helper @*/ Direction right() {
        switch (this) {
        case EAST:
            return Direction.SOUTH;
        case NORTH:
            return Direction.EAST;
        case SOUTH:
            return Direction.WEST;
        case WEST:
            return Direction.NORTH;
        }
        return null;
    }
}
