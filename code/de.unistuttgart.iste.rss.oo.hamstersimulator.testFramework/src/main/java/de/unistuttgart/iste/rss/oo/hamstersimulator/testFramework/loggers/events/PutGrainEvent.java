package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class PutGrainEvent extends LocationEvent {

    public PutGrainEvent(final long time, final Location tile) {
        super(time, tile);
    }
}
