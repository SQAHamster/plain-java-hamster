package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

/**
 * Delta which represents an added log entry
 */
public class AddLogEntryDelta extends Delta {
    private static final long serialVersionUID = -6341894296286153193L;

    /**
     * the message of the log entry
     */
    private final String message;
    /**
     * the id of the hamster associated with the log entry, might be null
     */
    private final Integer hamsterId;

    /**
     * Creates a new AddLogEntryDelta
     * @param message the message of the log entry, must be != null
     * @param hamsterId the id of the hamster associated with the log entry, might be null
     */
    public AddLogEntryDelta(final String message, final Integer hamsterId) {
        super(DeltaType.ADD_LOG_ENTRY);
        checkNotNull(message);

        this.message = message;
        this.hamsterId = hamsterId;
    }

}
