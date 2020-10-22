package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class TurnEvent extends LocationEvent {

    Direction oldDirection;
    Direction newDirection;

    public TurnEvent(final long time, final Location location, final Direction oldDirection, final Direction newDirection) {
        super(time, location);
        this.oldDirection = oldDirection;
        this.newDirection = newDirection;
    }
}
