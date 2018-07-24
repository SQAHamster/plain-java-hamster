package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Direction;

public class HamsterChangedDirectionEvent extends HamsterStateChangedEvent {

    private final Direction oldDirection;
    private final Direction newDirection;

    public HamsterChangedDirectionEvent(final Hamster hamster, final Direction oldDirection, final Direction newDirection) {
        super(hamster);
        this.oldDirection = oldDirection;
        this.newDirection = newDirection;
    }

    public Direction getOldDirection() {
        return oldDirection;
    }

    public Direction getNewDirection() {
        return newDirection;
    }

    @Override
    public String toString() {
        return "HamsterChangedDirectionEvent [oldDirection=" + oldDirection + ", newDirection=" + newDirection
                + ", toString()=" + super.toString() + "]";
    }
}
