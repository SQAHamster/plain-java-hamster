package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

/**
 * Interface to interact with a UI or mock object for reading values from the user.
 * @author Steffen Becker
 *
 */
public interface InputInterface {
    
    /**
     * Read an integer value from a user.
     * @return The integer value read.
     */
    public int readInteger();
    
    /** 
     * Read a string value from a user.
     * @return The string value read.
     */
    public String readString();
}
