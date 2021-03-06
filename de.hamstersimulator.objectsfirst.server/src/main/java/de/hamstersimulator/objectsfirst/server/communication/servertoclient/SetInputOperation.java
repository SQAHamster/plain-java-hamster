package de.hamstersimulator.objectsfirst.server.communication.servertoclient;

import de.hamstersimulator.objectsfirst.server.communication.InputOperation;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Operation to notify the client that the input with the specified id should be completed
 * with the specified result
 */
public class SetInputOperation extends InputOperation {
    private static final long serialVersionUID = 6722980492738342374L;

    /**
     * the result to complete the input
     */
    private final String result;

    /**
     * Creates a new SetInputOperation
     * @param inputId the id of the input to complete
     * @param result the result of the input
     */
    public SetInputOperation(final int inputId, final String result) {
        super(inputId);
        checkNotNull(result);

        this.result = result;
    }

    /**
     * Getter for the result
     * @return the result, != null, might be empty
     */
    public String getResult() {
        return result;
    }
}
