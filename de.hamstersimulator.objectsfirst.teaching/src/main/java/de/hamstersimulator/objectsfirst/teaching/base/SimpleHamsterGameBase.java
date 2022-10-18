package de.hamstersimulator.objectsfirst.teaching.base;

import de.hamstersimulator.objectsfirst.external.model.Territory;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;

/**
 * Special variant of the de.hamstersimulator.objectsfirst.teaching.lecture02.SimpleHamsterGame for the lecture. It is supposed to:
 * - Hide all stuff about Java's main method
 * - Hide all uses of protected operations
 * - Prevent the use of this references
 * Inheriting from this class allows to teach Java starting from the most
 * basic concepts as outlined in B. Meyer's book Touch of Class
 */
public abstract class SimpleHamsterGameBase extends SimpleHamsterGame {

    /**
     * Make the this reference available under a better name
     */
    protected SimpleHamsterGameBase game = this;

    /**
     * As a default setting, load the basic example territory introduced in the lecture.
     */
    public SimpleHamsterGameBase() {
        this.loadTerritoryFromResourceFile("/territory.ter");
    }

    /**
     * Operation to initialize the game into a running state.
     */
    public void initialize() {
        super.game.startGame();
    }

    /**
     * Method to display the hamster simulator's local JavaFX desktop UI
     * in a non-inspector mode.
     */
    public void displayInNewGameWindow() {
        super.displayInNewGameWindow();
    }

    /**
     * Provide simple access to the current territory.
     * @return The current game's territory.
     */
    public Territory getTerritory() {
        return super.game.getTerritory();
    }

}
