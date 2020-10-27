package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

public class RemoveLogEntryDelta extends Delta {
    private static final long serialVersionUID = 5627074189073188412L;

    public RemoveLogEntryDelta() {
        super(DeltaType.REMOVE_LOG_ENTRY);
    }
}
