package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

/**
 * observable version of command specification used to initialize the territory with a specific size
 */
public interface ObservableInitializeTerritoryCommandSpecification extends ObservableCommandSpecification {

    /**
     * Get the size of the newly initialized territory
     * @return the new size of the territory, != null
     */
    Size getSize();
}
