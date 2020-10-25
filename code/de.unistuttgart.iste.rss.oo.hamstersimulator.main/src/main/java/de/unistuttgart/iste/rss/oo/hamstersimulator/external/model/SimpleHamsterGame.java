package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.config.HamsterConfig;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Parent class of a simple, to a large extend preconfigured hamster game.
 * To be used in lectures 2-8 of PSE as predefined base class.
 *
 * @author Steffen Becker
 *
 */
public abstract class SimpleHamsterGame {

    /**
     * Name of the environment variable used to determine the output interface
     */
    private static final String OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME = "OUTPUT_INTERFACE";

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
        game.startGame();

        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    protected abstract void run();

    /**
     * Method to start a hamster game and handle any exceptions happening
     * while running.
     */
    public final void doRun() {
        try {
            this.run();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final RuntimeException e) {
            this.game.showAlert(e);
        }
        game.stopGame();
    }

    /**
     * Displays the hamster game in a new game window
     * The UI type can be specified in the config file or in the environment variable
     * OUTPUT_INTERFACE. Possible values are JAVA_FX, HTTP and NONE
     * The default is JAVA_FX.
     */
    protected void displayInNewGameWindow() {
        final UIMode mode = getUIModeFromEnvironmentVariable()
                .orElse(getUIModeFromConfig()
                        .orElse(UIMode.JAVA_FX));
        switch (mode) {
            case JAVA_FX:
                JavaFXUI.displayInNewGameWindow(this.game.getModelViewAdapter());
                break;
            case HTTP:
                // TODO
                break;
            case NONE:
                // ignore
                break;
            default:
                throw new IllegalStateException("not handled output interface case");
        }
    }

    /*@
     @ requires true;
     @ ensures game.getCurrentGameMode() == Mode.INITIALIZING;
     @*/
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
        final InputStream territoryFileStream = getClass().getResourceAsStream(fileName);
        checkArgument(territoryFileStream != null, "territory file not found");
        this.game.initialize(territoryFileStream);
    }

    /**
     * Loads the UI Mode from the config if possible
     * @return The UI mode if it was found in the default config, if the file is not present
     *         an empty optional
     * @throws IllegalStateException if something goes wrong reading the file or if the config file
     *         contains an illegal value
     */
    private Optional<UIMode> getUIModeFromConfig() {
        if (Files.exists(Path.of("config.json"))) {
            try {
                final HamsterConfig config = HamsterConfig.load();
                if (config.getOutput() != null) {
                    return Optional.of(UIMode.valueOf(config.getOutput()));
                } else {
                    return Optional.empty();
                }
            } catch (IOException | IllegalArgumentException e) {
                throw new IllegalStateException("Illegal config", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Loads the UI Mode from the environment variable if possible
     * @return The UI mode if the environment variable was set, otherwise an empty optional
     * @throws IllegalStateException if an illegal value is set
     */
    private Optional<UIMode> getUIModeFromEnvironmentVariable() {
        final String value = System.getenv(OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME);
        if (value != null) {
            try {
                return Optional.of(UIMode.valueOf(value));
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Illegal environmental variable", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * enum for different UI types
     */
    private enum UIMode {
        JAVA_FX,
        HTTP,
        NONE;
    }

}
