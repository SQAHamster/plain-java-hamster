package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

public class ModeChangedOperation implements Operation {
    private static final long serialVersionUID = 21726779971220461L;
    private final Mode mode;

    public ModeChangedOperation(final Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }
}
