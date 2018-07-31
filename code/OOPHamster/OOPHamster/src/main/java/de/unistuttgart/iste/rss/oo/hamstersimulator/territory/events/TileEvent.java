package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

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
