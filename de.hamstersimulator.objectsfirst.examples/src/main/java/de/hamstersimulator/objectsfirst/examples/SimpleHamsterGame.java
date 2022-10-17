package de.hamstersimulator.objectsfirst.examples;

import de.hamstersimulator.objectsfirst.external.model.Territory;

public abstract class SimpleHamsterGame
        extends de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame {

    protected static SimpleHamsterGame createInstance() {
        return new Example1();
    }
    protected SimpleHamsterGame game = this;

    public SimpleHamsterGame() {
        this.loadTerritoryFromResourceFile("/territory.ter");
    }

    public void initialize() {
        super.game.startGame();
    }

    public void displayInNewGameWindow() {
        super.displayInNewGameWindow();
    }

    protected Territory getTerritory() {
        return super.game.getTerritory();
    }

    /**
     * Main method used to start the simple hamster game.
     * @param args Default command line arguments, not used.
     */
    public static void main(final String[] args) {
        final SimpleHamsterGame example = createInstance();
        example.doRun();
    }
}
