package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

public class NewTerritoryDelta extends Delta {
    private static final long serialVersionUID = 1272925783034654704L;

    private final int width;
    private final int height;

    public NewTerritoryDelta(final int width, final int height) {
        super(DeltaType.NEW_TERRITORY);
        this.width = width;
        this.height = height;
    }
}
