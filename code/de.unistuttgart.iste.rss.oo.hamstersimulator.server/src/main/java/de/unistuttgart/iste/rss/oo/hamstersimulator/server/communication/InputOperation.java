package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication;

import java.io.Serializable;

public abstract class InputOperation implements Serializable {
    private final int inputId;

    protected InputOperation(final int inputId) {
        this.inputId = inputId;
    }

    public int getInputId() {
        return this.inputId;
    }
}
