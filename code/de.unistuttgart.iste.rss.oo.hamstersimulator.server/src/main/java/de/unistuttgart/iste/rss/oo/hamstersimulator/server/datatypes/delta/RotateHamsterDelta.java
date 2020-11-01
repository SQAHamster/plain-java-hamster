package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

public class RotateHamsterDelta extends Delta {
    private static final long serialVersionUID = 6203177399654550243L;

    private final int tileContentId;
    private final Direction direction;

    public RotateHamsterDelta(final int tileContentId, final Direction direction) {
        super(DeltaType.ROTATE_HAMSTER);
        this.tileContentId = tileContentId;
        this.direction = direction;
    }
}
