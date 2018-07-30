package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

public class TurnLeftCommand extends HamsterCommand {

    private final Direction previousDirection;

    public TurnLeftCommand(final Territory territory, final HamsterStateChanger stateChanger) {
        super(territory, stateChanger);
        this.previousDirection = this.hamster.getDirection();
    }

    @Override
    public void execute() {
        final Direction newDirection = previousDirection.left();
        stateChanger.setDirection(newDirection);
    }

    @Override
    public void undo() {
        stateChanger.setDirection(previousDirection);
    }

}
