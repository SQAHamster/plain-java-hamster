package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

public interface TerritoryListener extends TileListener {
    default void tileAdded(final TileAddedEvent e) {};
    default void tileRemoved(final TileRemovedEvent e) {};
}
