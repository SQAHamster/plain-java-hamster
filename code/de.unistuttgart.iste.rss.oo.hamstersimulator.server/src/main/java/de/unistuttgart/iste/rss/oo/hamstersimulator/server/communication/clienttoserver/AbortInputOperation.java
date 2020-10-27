package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

import java.io.Serializable;

public class AbortInputOperation extends InputOperation {
    private static final long serialVersionUID = -5404994225879638660L;

    public AbortInputOperation(final int inputId) {
        super(inputId);
    }
}
