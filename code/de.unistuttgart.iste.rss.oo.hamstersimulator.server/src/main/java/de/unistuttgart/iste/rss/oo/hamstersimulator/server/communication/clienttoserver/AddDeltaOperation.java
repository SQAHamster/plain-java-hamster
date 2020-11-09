package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.Delta;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Operation to inform the server that a single delta should be added to the deltasList
 */
public class AddDeltaOperation implements Operation {
    private static final long serialVersionUID = -3381780974437584031L;

    /**
     * the Delta to add
     */
    private final Delta delta;

    /**
     * Creates a new AddDeltaOperation
     * @param delta the delta to add, must be != null
     */
    public AddDeltaOperation(final Delta delta) {
        checkNotNull(delta);

        this.delta = delta;
    }

    /**
     * Getter for the delta which should be added
     * @return the delta, != null
     */
    public Delta getDelta() {
        return delta;
    }
}
