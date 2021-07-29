package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import java.util.Optional;

public class LogEntry {
    private final LogEntryType type;
    private Optional<String> message = Optional.empty();

    protected LogEntry(final LogEntryType type) {
        this.type = type;
    }

    public void setMessage(final String message) {
        this.message = Optional.of(message);
    }
}
