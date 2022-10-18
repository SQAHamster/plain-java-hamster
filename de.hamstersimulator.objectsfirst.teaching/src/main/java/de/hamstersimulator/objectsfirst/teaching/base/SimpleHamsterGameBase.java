package de.hamstersimulator.objectsfirst.teaching.base;

import de.hamstersimulator.objectsfirst.exceptions.GameAbortedException;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;
import de.hamstersimulator.objectsfirst.external.model.Territory;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

import java.io.InputStream;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkArgument;
import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Special variant of the de.hamstersimulator.objectsfirst.teaching.lecture02.SimpleHamsterGame for the lecture. It is supposed to:
 * - Hide all stuff about Java's main method
 * - Hide all uses of protected operations
 * - Prevent the use of this references
 * Inheriting from this class allows to teach Java starting from the most
 * basic concepts as outlined in B. Meyer's book Touch of Class
 */
public abstract class SimpleHamsterGameBase {

    /**
     * Make the this reference available under a better name
     */
    protected SimpleHamsterGameBase game = this;

    /**
     * The game object of this simple game. Can be used to start, stop, reset,
     * or display the game.
     */
    protected final HamsterGame hamsterGame = new HamsterGame();

    /**
     * Variable inherited to child classes containing the default hamster
     * which is named paule here. Intentionally, no getter or setter is used
     * as they are introduced only after lecture 2.
     */
    protected Hamster paule;

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
        hamsterGame.startGame();
        this.paule = hamsterGame.getTerritory().getDefaultHamster();
    }

    /**
     * Method to display the hamster simulator's local JavaFX desktop UI
     * in a non-inspector mode.
     */
    public void displayInNewGameWindow() {
        JavaFXUI.displayInNewGameWindow(hamsterGame.getModelViewAdapter());
    }

    /**
     * Provide simple access to the current territory.
     * @return The current game's territory.
     */
    public final Territory getTerritory() {
        return hamsterGame.getTerritory();
    }

    /**
     * Method to start the execution of a hamster game and handle any exceptions happening
     * while running.
     */
    public final void doRun() {
        try {
            this.abstractRun();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final Exception e) {
            hamsterGame.confirmAlert(e);
            throw e;
        } finally {
            hamsterGame.stopGame();
        }
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    protected abstract void abstractRun();

    /**
     * Loads the Territory from a resources file.
     * Only absolute resource paths are allowed. E.g. the fileName "/territory.ter" represents the file
     * territory.ter in the resources directory
     * This resets the game if it was already started. After the territory was loaded, the game is
     * in mode INITIALIZING. To start the game, game.startGame() should be called
     *
     * @param fileName An absolute path to the resource file. Must start with a "/"
     * @throws IllegalArgumentException if fileName is no absolute resource path (does not start with "/")
     *                                  or if the file was not found
     */
    protected final void loadTerritoryFromResourceFile(final String fileName) {
        checkNotNull(fileName);
        checkArgument(fileName.startsWith("/"), "fileName does not start with \"/\"");
        final InputStream territoryFileStream = this.getClass().getResourceAsStream(fileName);
        checkArgument(territoryFileStream != null, "territory file not found");
        hamsterGame.initialize(territoryFileStream);
    }
}
