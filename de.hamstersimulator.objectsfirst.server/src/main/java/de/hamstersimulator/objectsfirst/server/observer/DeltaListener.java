package de.hamstersimulator.objectsfirst.server.observer;

import de.hamstersimulator.objectsfirst.server.datatypes.delta.Delta;

import java.util.List;

/**
 * Listener for Deltas
 * Used in combination with TerritoryLogObserver: the observer observes changes on the
 * territory / log and creates deltas and notifies this listener about each new delta
 * via the onDeltasCreated method
 */
public interface DeltaListener {

    /**
     * Should be called by a TerritoryObserver when new deltas are added
     * @param deltaList a list with all new deltas, != null
     */
    void onDeltasCreated(final List<Delta> deltaList);
}
