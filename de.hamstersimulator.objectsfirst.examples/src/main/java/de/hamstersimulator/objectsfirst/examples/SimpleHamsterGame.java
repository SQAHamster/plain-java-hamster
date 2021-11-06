package de.hamstersimulator.objectsfirst.examples;

public abstract class SimpleHamsterGame
        extends de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame {

    protected static SimpleHamsterGame createInstance() {
        return new Example01();
    }

    /**
     * Main method used to start the simple hamster game.
     * @param args Default command line arguments, not used.
     */
    public static void main(final String[] args) {
        final SimpleHamsterGame example = createInstance();
        example.doRun();
    }
}
