package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * LogEntry representing the move of a Hamster
 */
public class MoveLogEntry extends HamsterLogEntry {
    public MoveLogEntry(final int hamsterId) {
        super(LogEntryType.MOVE, hamsterId);
    }
}
