package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;

/**
 * Operation to inform the client that the speed should be changed
 */
public class ChangeSpeedOperation implements Operation {
    private static final long serialVersionUID = -7312876621594396938L;

    /**
     * the new speed value
     */
    private final double speed;

    /**
     * Creates a new ChangeSpeedOperation
     * @param speed the new speed value, must be in range [0, 10]
     * @throws IllegalArgumentException if speed is not in range [0, 10]
     */
    public ChangeSpeedOperation(final double speed) {
        checkArgument(speed >= 0 && speed <= 10, "Provided speed is not in range [0, 10]");

        this.speed = speed;
    }

    /**
     * Getter for the new speed value
     * @return the new speed value, is in range [0, 10]
     */
    public double getSpeed() {
        return speed;
    }
}
