package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

public abstract class SimpleHamsterGame
        extends de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame {

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

    @Override
    protected void hamsterMain() {
        run();
    }

    abstract void run();
}
