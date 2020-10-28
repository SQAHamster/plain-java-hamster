package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication;

public class SpeedChangedOperation implements Operation {
    private static final long serialVersionUID = 4585005574432024351L;
    private final double speed;

    public SpeedChangedOperation(final double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
