package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * Log Entry representing that a Hamster put down a Grain
 */
public class PutGrainLogEntry extends HamsterLogEntry {
    public PutGrainLogEntry(final int hamsterId) {
        super(LogEntryType.PUT_GRAIN, hamsterId);
    }
}
