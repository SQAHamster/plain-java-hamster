package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Direction;
import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.Territory;

public class TurnLeftCommand extends HamsterCommand {

    private final Direction previousDirection;

    public TurnLeftCommand(final HamsterManipulator manipulator, final Territory territory) {
        super(manipulator, territory);
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
