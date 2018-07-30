package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

public interface TileListener {

    void contentItemAdded(TileContentAddedEvent e);
    void contentItemRemoved(TileContentRemovedEvent e);

}
