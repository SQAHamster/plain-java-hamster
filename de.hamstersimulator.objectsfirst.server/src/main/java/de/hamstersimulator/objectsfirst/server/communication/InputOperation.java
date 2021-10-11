package de.hamstersimulator.objectsfirst.server.communication;

/**
 * Base class for all input operations
 * input operations are operations which handle input, like aborting input,
 * and therefore refer to an input with a specific id
 * or requesting input, e.g. RequestInputOperation or AbortInputOperation
 */
public abstract class InputOperation implements Operation {
    /**
     * The id of the input to handle
     * The id is unique for each requested input and ensures that
     * HTTP requests handle the correct input and not an outdated one
     */
    private final int inputId;

    /**
     * Creates a new InputOperation with the specified id
     * @param inputId the id of the input to handle
     */
    protected InputOperation(final int inputId) {
        this.inputId = inputId;
    }

    /**
     * Getter for the inputId
     * The inputId identifies this input
     * @return the id
     */
    public int getInputId() {
        return this.inputId;
    }
}
