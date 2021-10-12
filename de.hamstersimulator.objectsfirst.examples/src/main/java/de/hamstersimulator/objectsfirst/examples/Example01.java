package de.hamstersimulator.objectsfirst.examples;

import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

/**
 * First hamster program used in lecture 2. The initial version used the predefined reference to
 * paule (the default hamster) and exemplifies calling methods on that object.
 *
 * @author Steffen Becker
 *
 */
class Example01 extends SimpleHamsterGame {

    /**
     * How often Paule should repeat its behaviour.
     */
    private static final int SCENARIO_REPETITION_COUNT = 20;

    /**
     * First hamster program. The idea is to move paule to the grain, let paule collect the grain,
     * and finally return to his initial tile.
     */
    @Override
    protected void run() {
        JavaFXUI.displayInNewGameWindow(game.getModelViewAdapter());
        game.initialize();
        for (int i = 0; i < SCENARIO_REPETITION_COUNT; i++) {
            paule.write("Hallo!");
            paule.move();
            paule.move();
            paule.pickGrain();
            paule.pickGrain();
            paule.putGrain();
            paule.putGrain();
            paule.turnLeft();
            paule.turnLeft();
            paule.move();
            paule.move();
            paule.turnLeft();
            paule.turnLeft();
        }
    }

}
