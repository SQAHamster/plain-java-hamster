package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication;

/**
 * Base class for all input operations
 */
public abstract class InputOperation implements Operation {
    /**
     * the id of the input to handle
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
     * @return the id
     */
    public int getInputId() {
        return this.inputId;
    }
}
