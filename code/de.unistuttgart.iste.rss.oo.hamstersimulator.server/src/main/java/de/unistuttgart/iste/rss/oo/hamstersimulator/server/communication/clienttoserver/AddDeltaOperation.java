package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.Delta;

public class AddDeltaOperation implements Operation {
    private static final long serialVersionUID = -3381780974437584031L;

    private final Delta delta;

    public AddDeltaOperation(final Delta delta) {
        this.delta = delta;
    }

    public Delta getDelta() {
        return delta;
    }
}
