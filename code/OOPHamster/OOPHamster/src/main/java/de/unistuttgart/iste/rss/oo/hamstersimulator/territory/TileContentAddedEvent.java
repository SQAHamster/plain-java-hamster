package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

public class TileContentAddedEvent extends TileEvent {

    private final TileContent newContent;

    public TileContentAddedEvent(final Tile tile, final TileContent newContent) {
        super(tile);
        this.newContent = newContent;
    }

    public TileContent getNewContent() {
        return newContent;
    }
}
