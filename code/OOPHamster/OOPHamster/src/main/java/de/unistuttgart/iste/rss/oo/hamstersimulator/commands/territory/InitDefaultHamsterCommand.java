package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public class InitDefaultHamsterCommand extends TerritoryCommand {

    private Direction previousDirection;
    private final Direction newDirection;
    private final int newGrainCount;
    private Location previousLocation;

    public InitDefaultHamsterCommand(final Territory territory, final Direction newDirection, final int newGrainCount) {
        super(territory);
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
    }

    @Override
    public void execute() {
        this.previousDirection = this.getTerritory().getDefaultHamster().getDirection();
        this.previousLocation = this.getTerritory().getDefaultHamster().getCurrentPosition().get();
        // TODO: this.previousGrains = this.getTerritory().getDefaultHamster().
        this.getTerritory().getDefaultHamster().init(null, this.newDirection, this.newGrainCount);
    }

    @Override
    public void undo() {
        this.getTerritory().getDefaultHamster().init(this.previousLocation, this.previousDirection, /* TODO */ 0);
    }

}
