package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class PlacedOnTerritoryEvent extends LocationEvent {
    public PlacedOnTerritoryEvent(final long time, final Location tile) {
        super(time, tile);
    }
}
