package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public abstract class TerritoryCommand extends Command {
    private final Territory territory;

    public TerritoryCommand(final Territory territory) {
        super();
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }
}
