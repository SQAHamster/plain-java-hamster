package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

public class RotateHamsterDelta extends Delta {
    private static final long serialVersionUID = 6203177399654550243L;

    private final int entityId;
    private final Direction direction;

    public RotateHamsterDelta(final int entityId, final Direction direction) {
        super(DeltaType.ROTATE_HAMSTER);
        this.entityId = entityId;
        this.direction = direction;
    }
}
