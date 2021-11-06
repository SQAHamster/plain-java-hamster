package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

/**
 * LogEntry which represents a Hamster added to a Tile
 */
public class InitHamsterLogEntry extends LogEntry {
    /**
     * Data for the Hamster which was added
     */
    private final HamsterData hamster;

    public InitHamsterLogEntry(final HamsterData hamster) {
        super(LogEntryType.INIT_HAMSTER);
        this.hamster = hamster;
    }
}
