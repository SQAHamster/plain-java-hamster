package de.hamstersimulator.objectsfirst.server.datatypes.delta;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.server.datatypes.type.DeltaType;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Delta which represents a rotated hamster
 */
public class RotateHamsterDelta extends Delta {
    private static final long serialVersionUID = 6203177399654550243L;

    /**
     * the idf of the hamster to rotate
     */
    private final int tileContentId;
    /**
     * the new direction of the hamster
     */
    private final Direction direction;

    /**
     * Creates a new RotateHamsterDelta
     * @param tileContentId the id of the hamster
     * @param direction the new direction of the hamster, must be != null
     */
    public RotateHamsterDelta(final int tileContentId, final Direction direction) {
        super(DeltaType.ROTATE_HAMSTER);
        checkNotNull(direction);

        this.tileContentId = tileContentId;
        this.direction = direction;
    }
}
