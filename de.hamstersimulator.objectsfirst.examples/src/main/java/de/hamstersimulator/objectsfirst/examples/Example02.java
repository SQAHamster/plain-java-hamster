package de.hamstersimulator.objectsfirst.examples;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

class Example02 extends SimpleHamsterGame {
    Hamster paula;

    /**
     * Another hamster program. The idea is to create Paula close to Paula with
     * grain in her mouth. She drops it and Paule picks it up.     
     */
    @Override
    protected void run() {
        game.initialize();
        JavaFXUI.displayInNewGameWindow(game.getModelViewAdapter());

        Hamster paula = new Hamster(game.getTerritory(), paule.getLocation(), paule.getDirection(), 0);

        paula.putGrain();
        paule.move();
        paule.pickGrain();
    }
}
