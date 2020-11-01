package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;


public class InitializeTerritoryDelta extends Delta {
    private final Size size;

    public InitializeTerritoryDelta(final Size size) {
        super(DeltaType.INITIALIZE_TERRITORY);
        this.size = size;
    }
}
