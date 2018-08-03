package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public abstract class TileEvent {
    private final Tile tile;

    public TileEvent(final Tile tile) {
        super();
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

}
