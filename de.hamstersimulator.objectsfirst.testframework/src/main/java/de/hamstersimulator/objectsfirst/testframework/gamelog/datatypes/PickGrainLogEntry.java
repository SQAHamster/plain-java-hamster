package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * LogEntry representing that a Hamster picked up a Grain
 */
public class PickGrainLogEntry extends HamsterLogEntry {
    public PickGrainLogEntry(final int hamsterId) {
        super(LogEntryType.PICK_GRAIN, hamsterId);
    }
}

