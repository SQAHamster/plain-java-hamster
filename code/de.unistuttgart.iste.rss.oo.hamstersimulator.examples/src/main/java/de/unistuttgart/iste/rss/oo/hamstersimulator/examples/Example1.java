package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

class Example1 extends SimpleHamsterGame {

    @Override
    protected void run() {
        JavaFXUI.displayInNewGameWindow(game.getModelViewAdapter());
        game.initialize();
        paule.move();
        paule.move();
        paule.pickGrain();
        paule.pickGrain();
        paule.turnLeft();
        paule.turnLeft();
    }

}
