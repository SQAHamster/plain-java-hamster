package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public abstract class TerritoryEvent {
    private final Territory territory;

    public TerritoryEvent(final Territory territory) {
        super();
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }
}
