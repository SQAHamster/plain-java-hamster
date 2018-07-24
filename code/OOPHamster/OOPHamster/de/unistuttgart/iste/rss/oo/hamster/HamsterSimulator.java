package de.unistuttgart.iste.rss.oo.hamster;

public class HamsterSimulator {

    private final Territory territory;
    private final CommandStack commandStack;

    public HamsterSimulator(final String territoryFileName) {
        super();
        this.commandStack = new CommandStack(this);
        this.territory = new Territory(this, territoryFileName);
        this.territory.buildTerritoryFromString(territoryFileName);
    }

    public Territory getTerritory() {
        return territory;
    }

    public CommandStack getCommandStack() {
        return this.commandStack;
    }

}