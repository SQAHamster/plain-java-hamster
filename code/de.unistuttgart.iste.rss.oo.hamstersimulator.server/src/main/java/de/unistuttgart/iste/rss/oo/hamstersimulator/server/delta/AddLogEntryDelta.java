package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

import java.util.Optional;

public class AddLogEntryDelta extends Delta {
    private static final long serialVersionUID = -6341894296286153193L;

    private final String message;
    private final Integer hamsterId;

    public AddLogEntryDelta(final String message, final Integer hamsterId) {
        super(DeltaType.ADD_LOG_ENTRY);
        this.message = message;
        this.hamsterId = hamsterId;
    }

}
