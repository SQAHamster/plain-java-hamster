package de.hamstersimulator.objectsfirst.teaching.lecture02;

class Example01 extends SimpleHamsterGame {

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
