package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

import java.io.Serializable;

/**
 * Operation to notify the server that the input with the specified id is aborted
 */
public class AbortInputOperation extends InputOperation {
    private static final long serialVersionUID = -5404994225879638660L;

    /**
     * Creates a new AbortInputOperation
     * @param inputId the id of the input which should be aborted
     */
    public AbortInputOperation(final int inputId) {
        super(inputId);
    }
}
