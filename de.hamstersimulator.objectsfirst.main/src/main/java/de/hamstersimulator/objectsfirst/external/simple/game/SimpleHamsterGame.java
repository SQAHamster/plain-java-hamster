package de.hamstersimulator.objectsfirst.external.simple.game;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.config.HamsterConfig;
import de.hamstersimulator.objectsfirst.exceptions.GameAbortedException;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;
import de.hamstersimulator.objectsfirst.server.http.client.HamsterClient;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkArgument;
import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Parent class of a simple, to a large extend preconfigured hamster game.
 * To be used in lectures 2-8 of PSE as predefined base class.
 *
 * @author Steffen Becker
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
     * The current SimpleHamsterGame. Can be used to load a territory or to
     * display the game in a new game window.
     */
    protected final SimpleHamsterGame currentGame = this;

    /**
     * Initialized a simple hamster game by loading a default territory
     * and setting protected references to contain the default hamster and
     * the game.
     */
    public SimpleHamsterGame() {
        this.game.startGame();

        this.paule = this.game.getTerritory().getDefaultHamster();
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    protected abstract void run();

    /**
     * Method to start the execution of a hamster game and handle any exceptions happening
     * while running.
     */
    public final void doRun() {
        try {
            this.run();
            this.postRun();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final Exception e) {
            this.game.confirmAlert(e);
            throw e;
        } finally {
            this.game.stopGame();
        }
    }

    /**
     * Executed after the run method was executed
     * Can be used, to perform cleanup, or start UI tools
     * Warning: might not be executed!
     */
    protected void postRun() {

    }

    /*@
     @ requires true;
     @ ensures (\return = UIMode.JAVA_FX) || (\return = UIMode.HTTP) || (\return = UIMode.NONE)
     */

    /**
     * Checks if a game UI mode is requested via environment variables or config and returns that mode in that priority.
     * <p>
     * If none is requested, the JAVA_FX mode will be requested.
     *
     * @return A non-null String which will be one of the strings in <code>UIMode</code>  of the currently requested mode,
     */
    protected String getRequestedUIMode() {
        return SimpleHamsterGame.getUIModeFromEnvironmentVariable()
                .orElse(SimpleHamsterGame.getUIModeFromConfig()
                        .orElse(UIMode.JAVA_FX));
    }

    /**
     * Displays the hamster game in a new game window
     * The UI type can be specified in the config file or in the environment variable
     * OUTPUT_INTERFACE. Possible values are JAVA_FX, HTTP and NONE
     * The default is JAVA_FX.
     */
    protected void displayInNewGameWindow() {
        this.openGameUserInterface(this.getRequestedUIMode());
    }

    /*@
     @ requires (mode == UIMode.JAVA_FX) || (mode == UIMode.HTTP) || (mode == UIMode.NONE);
     @*/

    /**
     * Starts the user interface specified by <code>mode</code> (must be one of the modes in `UIMode</code>)
     * <p>
     * Modes:
     * <ul>
     * <li>JAVA_FX: Starts the regular (default) JavaFX UI on the local display of the user</li>
     * <li>HTTP: Starts the HTTP server interface to use in the Browser or in the VSCode extension</li>
     * <li>NONE: Doesn't start any user interface for the simulator. E.g. for automated tests</li>
     * </ul>
     *
     * @param mode The User Interface mode to start for this <code>SimpleHamsterGame</code> as String.
     *             Can't be <code>null</code> and must be one of the Strings in `UIMode</code>
     * @throws IllegalArgumentException If <code>mode</code> is <code>null</code> or not one of the allowed values.
     */
    private void openGameUserInterface(final String mode) {
        switch (mode) {
            case UIMode.JAVA_FX:
                JavaFXUI.displayInNewGameWindow(this.game.getModelViewAdapter());
                break;
            case UIMode.HTTP:
                HamsterClient.startAndConnectToServer(this.game.getModelViewAdapter());
                break;
            case UIMode.NONE:
                // ignore
                break;
            default:
                throw new IllegalStateException("Unknown output interface type, possible values are: " +
                        UIMode.JAVA_FX + ", " + UIMode.HTTP + " or " + UIMode.NONE);
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
        final InputStream territoryFileStream = this.getClass().getResourceAsStream(fileName);
        checkArgument(territoryFileStream != null, "territory file not found");
        this.game.initialize(territoryFileStream);
    }

    /**
     * Returns the view model used by the game as an output interface
     * <p>
     * The view model provides information about the current game and can be used to (un)register input interfaces.
     * It is used for adapting the game to a user interface such as the JavaFX UI
     *
     * @return The view model used by the game in this simple hamster game. It is always the same instance and won't be null
     */
    public HamsterGameViewModel getGameViewModel() {
        return this.game.getModelViewAdapter();
    }

    /**
     * Loads the UI Mode from the config if possible
     *
     * @return The UI mode if it was found in the default config, if the file is not present
     * an empty optional
     * @throws IllegalStateException if something goes wrong reading the file or if the config file
     *                               contains an illegal value
     */
    private static Optional<String> getUIModeFromConfig() {
        if (Files.exists(Path.of("config.json"))) {
            try {
                final HamsterConfig config = HamsterConfig.load();
                if (config.getOutput() != null) {
                    return Optional.of(config.getOutput());
                } else {
                    return Optional.empty();
                }
            } catch (final IOException | IllegalArgumentException e) {
                throw new IllegalStateException("Illegal config", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Loads the UI Mode from the environment variable if possible
     *
     * @return The UI mode if the environment variable was set, otherwise an empty optional
     * @throws IllegalStateException if an illegal value is set
     */
    private static Optional<String> getUIModeFromEnvironmentVariable() {
        final String value = System.getenv(SimpleHamsterGame.OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME);
        if (value != null) {
            try {
                return Optional.of(value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException("Illegal environmental variable", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Different UI types
     * No enum is used because enums use reflection for valueOf which can cause issues if reflection is forbidden
     */
    protected static final class UIMode {
        public static final String JAVA_FX = "JAVA_FX";
        public static final String HTTP = "HTTP";
        public static final String NONE = "NONE";
    }

}
