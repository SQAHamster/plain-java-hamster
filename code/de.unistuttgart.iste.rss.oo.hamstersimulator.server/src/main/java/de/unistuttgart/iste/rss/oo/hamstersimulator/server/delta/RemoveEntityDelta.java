package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

public class RemoveEntityDelta extends Delta {
    private static final long serialVersionUID = 1368035267950332555L;

    private final int entityId;

    public RemoveEntityDelta(final int entityId) {
        super(DeltaType.REMOVE_ENTITY);
        this.entityId = entityId;
    }
}
