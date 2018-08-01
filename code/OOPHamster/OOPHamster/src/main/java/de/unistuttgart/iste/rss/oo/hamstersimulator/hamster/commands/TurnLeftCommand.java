package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;

public class TurnLeftCommand extends HamsterCommand {

    private final Direction previousDirection;

    public TurnLeftCommand(final HamsterStateChanger stateChanger) {
        super(stateChanger);
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
