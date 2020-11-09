package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.InputMessage;

public class RequestInputOperation implements Operation {

    private static final long serialVersionUID = 7728004034947790199L;

    private final InputMessage message;

    public RequestInputOperation(final InputMessage message) {
        this.message = message;
    }

    public InputMessage getMessage() {
        return message;
    }
}
