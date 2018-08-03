package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EntityCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public abstract class TerritoryCommand extends EntityCommand<Territory> {
    private final Territory territory;

    public TerritoryCommand(final PropertyMap<Territory> territoryState) {
        super(territoryState);
        this.territory = territoryState.getPropertyOwner();
    }

    public Territory getTerritory() {
        return territory;
    }
}
