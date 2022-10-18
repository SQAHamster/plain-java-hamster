package de.hamstersimulator.objectsfirst.teaching.lecture08;

import de.hamstersimulator.objectsfirst.teaching.base.SimpleHamsterGameBase;

public abstract class SimpleHamsterGame extends SimpleHamsterGameBase {
    /**
     * Main method used to start the simple hamster game.
     * @param args Default command line arguments, not used.
     */
    public static void main(final String[] args) {
        final SimpleHamsterGameBase example = new Example03();
        example.doRun();
    }
}
