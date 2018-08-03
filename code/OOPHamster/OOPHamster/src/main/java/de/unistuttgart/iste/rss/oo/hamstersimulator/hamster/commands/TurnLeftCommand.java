package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class TurnLeftCommand extends HamsterCommand {

    private Direction previousDirection;

    public TurnLeftCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    public void execute() {
        this.previousDirection = this.hamster.getDirection();
        final Direction newDirection = previousDirection.left();
        this.entityState.getObjectProperty("direction").set(newDirection);
    }

    @Override
    public void undo() {
        this.entityState.getObjectProperty("direction").set(previousDirection);
    }

}
