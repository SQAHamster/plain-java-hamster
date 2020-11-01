package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableGrain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTileContent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableWall;

public enum TileContentType {
    HAMSTER,
    WALL,
    GRAIN;

    public static TileContentType fromObservable(final ObservableTileContent observable) {
        if (observable instanceof ObservableHamster) {
            return HAMSTER;
        } else if (observable instanceof ObservableWall) {
            return WALL;
        } else if (observable instanceof ObservableGrain) {
            return GRAIN;
        } else {
            throw new IllegalStateException("unknown observable");
        }
    }
}
