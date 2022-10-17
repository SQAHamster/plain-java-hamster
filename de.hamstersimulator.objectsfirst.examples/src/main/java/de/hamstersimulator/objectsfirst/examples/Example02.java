package de.hamstersimulator.objectsfirst.examples;
import de.hamstersimulator.objectsfirst.external.model.Hamster;

class Example02 extends SimpleHamsterGame {
    /**
     * Another hamster program. The idea is to create Paula close to Paula with
     * grain in her mouth. She drops it and Paule picks it up.     
     */
    @Override
    protected void run() {
        game.displayInNewGameWindow();
        game.initialize();

        Hamster paula = new Hamster(game.getTerritory(), paule.getLocation(), paule.getDirection(), 0);

        paula.putGrain();
        paule.move();
        paule.pickGrain();
    }
}
