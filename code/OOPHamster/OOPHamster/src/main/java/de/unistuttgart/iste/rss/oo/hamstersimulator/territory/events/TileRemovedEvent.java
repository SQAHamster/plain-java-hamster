package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class TileRemovedEvent extends TerritoryTileEvent {

    public TileRemovedEvent(final Territory territory, final Tile tile) {
        super(territory, tile);
    }

}
