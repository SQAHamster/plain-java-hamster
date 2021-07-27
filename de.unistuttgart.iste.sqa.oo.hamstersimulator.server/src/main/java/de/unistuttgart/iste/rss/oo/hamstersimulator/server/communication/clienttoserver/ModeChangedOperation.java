package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

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
