package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

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
