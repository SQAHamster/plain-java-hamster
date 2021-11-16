package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * LogEntry representing that a Hamster turned left
 */
public class TurnLeftLogEntry extends HamsterLogEntry {
    public TurnLeftLogEntry(final int hamsterId) {
        super(LogEntryType.TURN_LEFT, hamsterId);
    }
}
