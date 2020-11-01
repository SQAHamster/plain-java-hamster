package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

public class NewTerritoryDelta extends Delta {
    private static final long serialVersionUID = 1272925783034654704L;

    private final int width;
    private final int height;

    public NewTerritoryDelta(final int width, final int height) {
        super(DeltaType.INITIALIZE_TERRITORY);
        this.width = width;
        this.height = height;
    }
}
