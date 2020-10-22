package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class LocationEvent extends HamsterEvent {

    private final Location tile;

    public LocationEvent(final long time, final Location tile) {
        super(time);
        this.tile = tile;
    }

    public Location getTile() {
        return this.tile;
    }
}
