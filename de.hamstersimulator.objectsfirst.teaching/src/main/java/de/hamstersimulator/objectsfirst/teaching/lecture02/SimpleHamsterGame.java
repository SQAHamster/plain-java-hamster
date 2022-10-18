package de.hamstersimulator.objectsfirst.teaching.lecture02;

import de.hamstersimulator.objectsfirst.teaching.base.SimpleHamsterGameBase;

/**
 * The only purpose of this class is to prevent imports and/or package
 * statements in Example01
 */
public abstract class SimpleHamsterGame extends SimpleHamsterGameBase {
    /**
     * Main method used to start the simple hamster game.
     * @param args Default command line arguments, not used.
     */
    public static void main(final String[] args) {
        final SimpleHamsterGame example = new Example01();
        example.doRun();
    }
}
