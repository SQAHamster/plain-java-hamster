package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

/**
 * First hamster program used in lecture 2. The initial version used the predefined reference to
 * paule (the default hamster) and exemplifies calling methods on that object.
 *
 * @author Steffen Becker
 *
 */
class Example02 extends SimpleHamsterGame {

    /**
     * How often Paule should repeat its behaviour.
     */
    private static final int SCENARIO_REPETITION_COUNT = 20;

    /**
     * Create a new instance of Example02
     */
    public Example02() {
        game.initialize();
        game.startGame(false);
        displayInNewGameWindow();
    }

    /**
     * First hamster program. The idea is to move paule to the grain, let paule collect the grain,
     * and finally return to his initial tile.
     */
    @Override
    protected void run() {
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
