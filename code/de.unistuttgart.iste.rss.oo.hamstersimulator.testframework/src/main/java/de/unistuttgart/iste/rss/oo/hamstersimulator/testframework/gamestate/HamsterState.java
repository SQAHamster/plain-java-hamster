package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

/**
 * Objects of this immutable class capture the state of a hamster
 * at a given point in time. A hamster state consists of the hamster's
 * looking direction, its location, and the number of grains collected or dropped.
 * @author Steffen Becker
 *
 */
public class HamsterState {

    /**
     * Hamster looking direction.
     */
    private final Direction direction;

    /**
     * Hamster location in its territory.
     */
    private final Location location;

    /**
     * The number of grains picked up by the hamster since it was initialized.
     */
    private final int grainCollected;

    /**
     * The number of grains dropped by the hamster since it was initialized.
     */
    private final int grainDropped;

    /**
     * Constructor for a new hamster state object.
     * @param newDirection The looking direction of the hamster.
     * @param newLocation The location of the hamster.
     * @param newGrainCollected The number of grains picked up by the hamster since it was initialized.
     * @param newGrainDropped The number of grains dropped by the hamster science it was initalized.
     */
    public HamsterState(final Direction newDirection, final Location newLocation, final int newGrainCollected, final int newGrainDropped) {
        super();
        this.direction = newDirection;
        this.location = newLocation;
        this.grainCollected = newGrainCollected;
        this.grainDropped = newGrainDropped;
    }

    /**
     * @return Looking direction of the hamster in this state.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return Location of the hamster on the territory in this state.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return Number of grains collected from the initialization of the hamster until this state.
     */
    public int getGrainCollected() {
        return grainCollected;
    }

    /**
     * @return Number of grains dropped from the initialization of the hamster until this state.
     */
    public int getGrainDropped() {
        return grainDropped;
    }

}
