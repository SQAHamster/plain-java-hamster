package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.Location;
import de.unistuttgart.iste.rss.oo.hamster.LocationVector;
import de.unistuttgart.iste.rss.oo.hamster.Territory;

public class MoveCommand extends HamsterCommand {

    private final Location previousHamsterPosition;

    public MoveCommand(final HamsterManipulator manipulator, final Territory territory) {
        super(manipulator, territory);
        this.previousHamsterPosition = hamster.getCurrentPosition();
    }

    @Override
    public void execute() {
        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = previousHamsterPosition.translate(movementVector);
        hamsterManipulator.setLocation(newHamsterPosition);
    }

    @Override
    public void undo() {
        hamsterManipulator.setLocation(previousHamsterPosition);
    }

}
