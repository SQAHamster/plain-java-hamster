package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * LogEntry representing that a message was written to the log
 */
public class WriteLogEntry extends HamsterLogEntry {
    /**
     * Message written to the log
     */
    private final String message;

    public WriteLogEntry(final int hamsterId, final String message) {
        super(LogEntryType.WRITE, hamsterId);
        this.message = message;
    }
}
