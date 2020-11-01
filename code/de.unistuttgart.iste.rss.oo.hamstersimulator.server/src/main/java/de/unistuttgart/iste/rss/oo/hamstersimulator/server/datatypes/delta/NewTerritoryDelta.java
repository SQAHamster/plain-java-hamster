package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

public class NewTerritoryDelta extends Delta {
    private static final long serialVersionUID = 1272925783034654704L;

    private final int width;
    private final int height;

    public NewTerritoryDelta(final Size size) {
        super(DeltaType.NEW_TERRITORY);
        this.width = size.getColumnCount();
        this.height = size.getRowCount();
    }
}
