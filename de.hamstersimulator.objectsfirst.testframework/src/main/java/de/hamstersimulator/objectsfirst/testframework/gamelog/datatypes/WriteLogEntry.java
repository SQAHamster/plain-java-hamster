package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

public class WriteLogEntry extends HamsterLogEntry {
    private final String message;

    public WriteLogEntry(final int hamsterId, final String message) {
        super(LogEntryType.WRITE, hamsterId);
        this.message = message;
    }
}
