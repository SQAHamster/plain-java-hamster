package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EntityCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.InjectableCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public abstract class TerritoryCommand extends EntityCommand<Territory>
implements InjectableCommand<Territory> {

    private Territory territory;

    @Override
    public void setContext(final PropertyMap<Territory> territoryState) {
        this.entityState = territoryState;
        this.territory = territoryState.getPropertyOwner();
    }

    public Territory getTerritory() {
        return territory;
    }
}
