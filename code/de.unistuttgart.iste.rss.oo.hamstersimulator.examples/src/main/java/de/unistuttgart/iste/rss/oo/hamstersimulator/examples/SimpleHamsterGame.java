package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;

public abstract class SimpleHamsterGame
        extends de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame {

    private static SimpleHamsterGame createInstance() {
        Class<?> clazz = MethodHandles.lookup().lookupClass();
        try {
            return (SimpleHamsterGame) clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            throw new RuntimeException(e);
        }
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