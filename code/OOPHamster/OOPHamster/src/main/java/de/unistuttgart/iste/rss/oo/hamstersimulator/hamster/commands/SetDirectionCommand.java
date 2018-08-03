package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class SetDirectionCommand extends HamsterCommand {

    private Direction previousDirection;
    private final Direction newDirection;

    public SetDirectionCommand(final PropertyMap<Hamster> hamsterState, final Direction newDirection) {
        super(hamsterState);
        this.newDirection = newDirection;
    }

    @Override
    public void execute() {
        this.previousDirection = this.hamster.getDirection();
        this.entityState.getObjectProperty("direction").set(newDirection);
    }

    @Override
    public void undo() {
        this.entityState.getObjectProperty("direction").set(previousDirection);
    }

}
