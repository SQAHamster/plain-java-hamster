package de.hamstersimulator.objectsfirst.examples;

import de.hamstersimulator.objectsfirst.inspector.InspectableSimpleHamsterGame;

public class InspectorExample extends InspectableSimpleHamsterGame {

    public static void main(String[] args) {
        InspectorExample game = new InspectorExample();
        game.game.initialize();
        game.displayInNewGameWindow();
        game.doRun();
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    @Override
    protected void run() {
        this.game.startGame();
        for (int i = 0; i < 5; i++) {
            this.paule.turnLeft();
        }
    }
}
