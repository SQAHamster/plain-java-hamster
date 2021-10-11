package de.hamstersimulator.objectsfirst.server.observer;

import de.hamstersimulator.objectsfirst.server.datatypes.delta.Delta;

import java.util.List;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Base class for observers
 * provides functionality to notify a DeltaListener about new deltas
 */
public abstract class Observer {
    /**
     * the listener which listens for new deltas
     */
    private final DeltaListener listener;

    /**
     * Creates a new Observer
     * @param listener the listener which should be notifyed about new deltas, must be != null
     */
    protected  Observer(final DeltaListener listener) {
        checkNotNull(listener);

        this.listener = listener;
    }

    /**
     * Subclasses should use this
     * @param deltaList the list with new deltas
     */
    protected void notifyListener(final List<Delta> deltaList) {
        this.listener.onDeltasCreated(deltaList);
    }
}
