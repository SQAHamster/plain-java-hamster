package de.hamstersimulator.objectsfirst.server.datatypes.delta;

import de.hamstersimulator.objectsfirst.server.datatypes.type.DeltaType;

/**
 * Delta which represents a removed log entry
 * the removed entry is always the newest entry
 */
public class RemoveLogEntryDelta extends Delta {
    private static final long serialVersionUID = 5627074189073188412L;

    /**
     * Creates a new RemoveLogEntryDelta
     */
    public RemoveLogEntryDelta() {
        super(DeltaType.REMOVE_LOG_ENTRY);
    }
}
