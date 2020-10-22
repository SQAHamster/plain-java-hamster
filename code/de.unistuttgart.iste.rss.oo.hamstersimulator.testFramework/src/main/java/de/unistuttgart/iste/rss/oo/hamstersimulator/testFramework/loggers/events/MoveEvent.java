package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;

public class MoveEvent extends MultiMoveEvent {

    public MoveEvent(final long time, final ObservableTile before, final ObservableTile after) {
        super(time, before, after);
        if (super.getSteps() != 1) {
            throw new IllegalArgumentException("The step size isn't exactly one!");
        }
    }

    @Override
    public int getSteps() {
        return 1;
    }

    public Direction getDirection() {
        final int dX = this.getDeltaX();
        final int dY = this.getDeltaY();
        if (dX == 1) {
            return Direction.EAST;
        } else if (dX == -1) {
            return Direction.WEST;
        } else if (dX == 0) {
            if (dY == 1) {
                return Direction.SOUTH;
            } else if (dY == -1) {
                return Direction.NORTH;
            }
        }
        throw new IllegalStateException("The move is larger than one tile");
    }
}
