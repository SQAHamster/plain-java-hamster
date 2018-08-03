package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

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
