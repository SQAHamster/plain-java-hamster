package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes;

/**
 * Enum for the possible input types
 */
public enum InputMode {
    /**
     * No input is expected
     */
    NONE,
    /**
     * Currently an int is requested
     */
    READ_INT,
    /**
     * Currently a string is requested
     */
    READ_STRING,
    /**
     * A alter should be shown, no input requested
     */
    SHOW_ALERT
}
