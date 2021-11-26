package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * Log entry for the initial state, necessary because error can be
 * associated with the initial GameState
 */
public class InitialLogEntry extends LogEntry {
    public InitialLogEntry() {
        super(LogEntryType.INITIAL);
    }
}