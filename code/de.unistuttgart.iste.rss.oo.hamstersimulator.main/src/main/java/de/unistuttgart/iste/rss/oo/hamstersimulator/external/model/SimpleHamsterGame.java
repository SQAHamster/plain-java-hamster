package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;
import java.io.Console;

import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

/**
 * Parent class of a simple, to a large extend preconfigured hamster game.
 * To be used in lectures 2-8 of PSE as predefined base class.
 *
 * @author Steffen Becker
 *
 */
public abstract class SimpleHamsterGame {

    /**
     * Variable inherited to child classes containing the default hamster
     * which is named paule here. Intentionally, no getter or setter is used
     * as they are introduced only after lecture 2.
     */
    protected final Hamster paule;

    /**
     * The game object of this simple game. Can be used to start, stop, reset,
     * or display the game.
     */
    protected final HamsterGame game = new HamsterGame();

    /**
     * A console object to demonstrate IO besides using the read or write methods
     * of hamsters.
     */
    protected final Console console = System.console();

    /**
     * Initialized a simple hamster game by loading a default territory
     * and setting protected references to contain the default hamster and
     * the game.
     */
    public SimpleHamsterGame() {
        JavaFXUI.displayInNewGameWindow(game.getAdapter());
        game.startGame(false);

        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    protected abstract void hamsterMain();

    /**
     * Internal method to start a hamster game and handle any exceptions happening
     * while running.
     */
    protected final void doRun() {
        try {
            this.hamsterMain();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final RuntimeException e) {
            this.game.showAlert(e);
        }
        game.stopGame();
    }

}
