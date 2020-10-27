package de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Helper class for json serialization, represents a message in the log
 * Also the base class for InputMessage
 */
public class LogMessage {
    private final String message;
    private final int id;

    /**
     * Creates a new instance of a LogMessage
     * @param message the message, must be != null
     * @param id the id of the message
     */
    public LogMessage(final String message, final int id) {
        checkNotNull(message, "message must not be null");

        this.message = message;
        this.id = id;
    }
}
