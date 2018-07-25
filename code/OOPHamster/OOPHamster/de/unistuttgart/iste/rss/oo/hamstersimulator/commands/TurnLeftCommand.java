package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterManipulator;

public class TurnLeftCommand extends HamsterCommand {

    private final Direction previousDirection;

    public TurnLeftCommand(final HamsterManipulator manipulator) {
        super(manipulator);
        this.previousDirection = this.hamster.getDirection();
    }

    @Override
    public void execute() {
        final Direction newDirection = previousDirection.left();
        hamsterManipulator.setDirection(newDirection);
    }

    @Override
    public void undo() {
        hamsterManipulator.setDirection(previousDirection);
    }

}
