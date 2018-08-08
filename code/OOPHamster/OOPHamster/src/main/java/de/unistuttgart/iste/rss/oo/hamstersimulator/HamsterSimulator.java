package de.unistuttgart.iste.rss.oo.hamstersimulator;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public class HamsterSimulator {

    private final Territory territory;
    private final CommandStack commandStack;

    public HamsterSimulator() {
        super();
        this.commandStack = new CommandStack(this);
        this.territory = new Territory();
    }

    public Territory getTerritory() {
        return territory;
    }

    public CommandStack getCommandStack() {
        return this.commandStack;
    }

}