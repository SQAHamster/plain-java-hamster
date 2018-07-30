package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

public interface TerritoryListener {
    void territoryResized(TerritoryResizedEvent e);
    void tileAdded(TileAddedEvent e);
    void tileRemoved(TileRemovedEvent e);
}
