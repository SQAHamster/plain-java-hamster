package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.Delta;

import java.io.Serializable;

public class AddDeltaOperation implements Serializable {
    private static final long serialVersionUID = -3381780974437584031L;

    private final Delta delta;

    public AddDeltaOperation(final Delta delta) {
        this.delta = delta;
    }

    public Delta getDelta() {
        return delta;
    }
}
