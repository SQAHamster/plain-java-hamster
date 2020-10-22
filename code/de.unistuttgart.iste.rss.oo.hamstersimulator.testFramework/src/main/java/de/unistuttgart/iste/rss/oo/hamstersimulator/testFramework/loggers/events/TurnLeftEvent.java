package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class TurnLeftEvent extends TurnEvent {

    public TurnLeftEvent(final long time, final Location location, final Direction newDirection) {
        super(time, location, newDirection.right(), newDirection);
    }
}
