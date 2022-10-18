package de.hamstersimulator.objectsfirst.examples.inspector;

import de.hamstersimulator.objectsfirst.inspector.InspectableSimpleHamsterGame;

/**
 * This class demonstrates how to start the hamster simulator in the
 * inspector mode. In this mode, users can see the classes and objects
 * used in the simulator and they can manipulate them manually at runtime in
 * a similar way as the Greenfoot IDE does.
 */
public class InspectorExample extends InspectableSimpleHamsterGame {

    /**
     * Main method which instantiates a new hamster game using the
     * object inspector mode and runs it.
     * @param args Not used in this case
     */
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
