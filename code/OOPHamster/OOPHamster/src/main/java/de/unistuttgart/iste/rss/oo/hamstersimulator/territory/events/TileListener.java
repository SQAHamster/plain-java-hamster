package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

public interface TileListener {
    default void contentItemAdded(final TileContentAddedEvent e) {};
    default void contentItemRemoved(final TileContentRemovedEvent e) {};
}
