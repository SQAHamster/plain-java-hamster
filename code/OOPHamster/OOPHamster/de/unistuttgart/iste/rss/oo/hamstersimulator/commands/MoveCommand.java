package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterManipulator;

public class MoveCommand extends HamsterCommand {

    private final Location previousHamsterPosition;

    public MoveCommand(final HamsterManipulator manipulator) {
        super(manipulator);
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
