package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

class Example1 extends SimpleHamsterGame {

    @Override
    void run() {
        game.displayInNewGameWindow();
        game.initialize();
        paule.move();
        paule.move();
        paule.pickGrain();
        paule.pickGrain();
        paule.turnLeft();
        paule.turnLeft();
    }

}
