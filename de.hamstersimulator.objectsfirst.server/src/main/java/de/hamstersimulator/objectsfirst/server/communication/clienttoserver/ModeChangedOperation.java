package de.hamstersimulator.objectsfirst.server.communication.clienttoserver;

import de.hamstersimulator.objectsfirst.datatypes.Mode;
import de.hamstersimulator.objectsfirst.server.communication.Operation;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Operation to notify the client that the mode changed
 */
public class ModeChangedOperation implements Operation {
    private static final long serialVersionUID = 21726779971220461L;

    /**
     * the new mode
     */
    private final Mode mode;

    /**
     * Creates a new ModeChangedOperation
     * @param mode the new mode, must be != null
     */
    public ModeChangedOperation(final Mode mode) {
        checkNotNull(mode);

        this.mode = mode;
    }

    /**
     * Getter for the new mode
     * @return the new mode, != null
     */
    public Mode getMode() {
        return mode;
    }
}
