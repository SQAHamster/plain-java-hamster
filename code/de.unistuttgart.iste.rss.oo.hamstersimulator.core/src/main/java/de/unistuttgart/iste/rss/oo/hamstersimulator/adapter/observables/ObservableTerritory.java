package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Interface to observe changes on the internal territory
 * Provides read-only access to all tiles of the territory, and therefore all
 * contents on the territory
 */
public interface ObservableTerritory {
    /**
     * Getter for the territorySize property
     * Can be used to get the current size of the territory, which
     * provides the amount of rows and columns
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<Size> territorySizeProperty();

    /**
     * Getter for the tiles property
     * Can be used to observe changes on any tile of the territory
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableTile> tilesProperty();

    /**
     * Getter for the hamsters property
     * Can be used to observe changes on any hamster on the territory
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableHamster> hamstersProperty();

    /**
     * Getter for the defaultHamster property
     * The default hamster always exists, so the property is always set
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<? extends ObservableHamster> defaultHamsterProperty();

}
