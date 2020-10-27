package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.EntityType;

public class AddEntityDelta extends Delta {
    private static final long serialVersionUID = 4014642903948847027L;

    final EntityType entityType;

    public AddEntityDelta(final EntityType entityType) {
        super(DeltaType.ADD_ENTITY);
        this.entityType = entityType;
    }
}
