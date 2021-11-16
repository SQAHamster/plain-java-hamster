package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * Base class for all LogEntries which affect a hamster
 */
public class HamsterLogEntry extends LogEntry {
    /**
     * The id of the hamster which is affected
     */
    private final int hamsterId;

    protected HamsterLogEntry(final LogEntryType type, final int hamsterId) {
        super(type);
        this.hamsterId = hamsterId;
    }
}
