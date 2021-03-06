package de.hamstersimulator.objectsfirst.server.communication.clienttoserver;

import de.hamstersimulator.objectsfirst.server.communication.Operation;
import de.hamstersimulator.objectsfirst.server.input.InputMessage;

/**
 * Operation to notify the server that a new input is requested
 */
public class RequestInputOperation implements Operation {

    private static final long serialVersionUID = 7728004034947790199L;

    /**
     * the new requested input
     */
    private final InputMessage message;

    /**
     * Creates a new RequestInputOperation
     * @param message the new input message, must be != null
     */
    public RequestInputOperation(final InputMessage message) {
        this.message = message;
    }

    /**
     * Getter for the new input message
     * @return the new message, != null
     */
    public InputMessage getMessage() {
        return message;
    }
}
