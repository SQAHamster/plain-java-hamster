package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

/**
 * Basic example of a SimpleHamsterGame which loads a territory and lets paule
 * move a bit and pick up grains
 */
class Example01 extends SimpleHamsterGame {

    /**
     * Creates a new instance of Example01
     */
    public Example01() {
        game.initialize();
        game.startGame(false);
        displayInNewGameWindow();
    }

    @Override
    protected void run() {
        paule.move();
        paule.move();
        paule.pickGrain();
        paule.pickGrain();
        paule.turnLeft();
        paule.turnLeft();
    }

}
