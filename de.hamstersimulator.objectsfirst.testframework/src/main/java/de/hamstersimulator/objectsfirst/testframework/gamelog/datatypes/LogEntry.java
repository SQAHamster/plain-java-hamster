package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import java.util.Optional;

public class LogEntry {
    private final LogEntryType type;
    private String errorMessage;

    protected LogEntry(final LogEntryType type) {
        this.type = type;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
