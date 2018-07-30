package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

public abstract class TerritoryTileEvent extends TerritoryEvent {

    private final Tile tile;

    public TerritoryTileEvent(final Territory territory, final Tile tile) {
        super(territory);
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

}
