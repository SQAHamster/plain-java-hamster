package de.hamstersimulator.objectsfirst.examples;

class Example1 extends SimpleHamsterGame {

    @Override
    protected void run() {
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
