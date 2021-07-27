package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableGrain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTileContent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableWall;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

/**
 * Enum with all possible tile content types
 */
public enum TileContentType {
    HAMSTER,
    WALL,
    GRAIN;

    /**
     *
     * @param observable the observable to get the TileContentType from, must be != null
     * @return the TileContentType which represents observable
     */
    public static TileContentType fromObservable(final ObservableTileContent observable) {
        checkNotNull(observable);

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
