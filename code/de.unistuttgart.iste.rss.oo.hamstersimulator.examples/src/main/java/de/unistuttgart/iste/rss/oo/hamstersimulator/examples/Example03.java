package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

class Example03 extends SimpleHamsterGame {
    Hamster paula;

    /**
     * Creates a new instance of Example03
     */
    public Example03() {
        game.initialize();
        game.startGame(false);
        displayInNewGameWindow();
    }

    /**
     * Another hamster program. The idea is to create Paula close to Paula with
     * grain in her mouth. She drops it and Paule picks it up.
     */
    @Override
    protected void run() {
        Hamster paula = new Hamster(game.getTerritory(), paule.getLocation(), paule.getDirection(), 0);

        paula.putGrain();
        paule.move();
        paule.pickGrain();
    }
}
