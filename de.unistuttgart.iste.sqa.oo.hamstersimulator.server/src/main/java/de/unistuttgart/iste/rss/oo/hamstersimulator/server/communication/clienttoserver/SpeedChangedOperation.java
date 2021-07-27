package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkArgument;

/**
 * Operation to notify the server that the speed changed
 */
public class SpeedChangedOperation implements Operation {
    private static final long serialVersionUID = 4585005574432024351L;

    /**
     * the new speed, must be a valid speed
     */
    private final double speed;

    /**
     * Create a new SpeedChangedOperation
     * @param speed the new value for speed, must be in range [0, 10]
     * @throws IllegalArgumentException if speed is not in range [0, 10]
     */
    public SpeedChangedOperation(final double speed) {
        checkArgument(speed >= 0 && speed <= 10, "Provided speed is not in range [0, 10]");

        this.speed = speed;
    }

    /**
     * Getter for the new speed
     * @return the new speed value, in range [0, 10]
     */
    public double getSpeed() {
        return speed;
    }
}
