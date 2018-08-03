package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class TileAddedEvent extends TerritoryTileEvent {

    public TileAddedEvent(final Territory territory, final Tile tile) {
        super(territory, tile);
    }

}
