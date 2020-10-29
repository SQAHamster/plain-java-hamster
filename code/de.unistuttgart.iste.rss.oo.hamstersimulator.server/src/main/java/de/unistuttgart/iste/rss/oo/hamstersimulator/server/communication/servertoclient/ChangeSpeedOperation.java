package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;

public class ChangeSpeedOperation implements Operation {
    private static final long serialVersionUID = -7312876621594396938L;
    private final double speed;

    public ChangeSpeedOperation(final double speed) {
        checkArgument(speed >= 0 && speed <= 10, "Provided speed is not in range [0, 10]");

        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
