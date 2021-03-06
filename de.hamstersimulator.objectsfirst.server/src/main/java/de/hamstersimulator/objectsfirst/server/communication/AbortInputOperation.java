package de.hamstersimulator.objectsfirst.server.communication;

/**
 * Operation to notify the server that the input with the specified id is aborted /
 * Operation to notify the client that the input with the specified id should be aborted
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
