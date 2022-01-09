package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * Base class of all LogEntries
 */
public class LogEntry {
    /**
     * The type of LogEntry
     * Necessary because of JSON serialization
     */
    private final LogEntryType type;
    /**
     * An associated error message
     */
    private String errorMessage;

    protected LogEntry(final LogEntryType type) {
        this.type = type;
    }

    /**
     * Sets the error message for this step of the execution
     *
     * @param errorMessage the new error message
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
