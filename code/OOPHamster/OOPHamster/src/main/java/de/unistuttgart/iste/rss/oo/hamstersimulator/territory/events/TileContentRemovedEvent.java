package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;

public class TileContentRemovedEvent extends TileEvent {

    private final TileContent removedContent;

    public TileContentRemovedEvent(final Tile tile, final TileContent removedContent) {
        super(tile);
        this.removedContent = removedContent;
    }

    public TileContent getRemovedContent() {
        return removedContent;
    }

}
