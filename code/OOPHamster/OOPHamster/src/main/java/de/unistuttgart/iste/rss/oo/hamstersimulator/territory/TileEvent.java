package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

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
