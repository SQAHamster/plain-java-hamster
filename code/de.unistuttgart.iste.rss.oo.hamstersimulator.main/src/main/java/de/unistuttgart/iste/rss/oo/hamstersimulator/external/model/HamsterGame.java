package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.DummyInputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.AbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * A class representing an instance of a hamster game. A hamster game consists of a territory, on
 * which the game takes place, a game log for messages, and a command stack for keeping track of the
 * game's history.
 * @author Steffen Becker
 *
 */
public class HamsterGame {

    /**
     * Maximum number for the game speed of the hamster game.
     */
    private static final double MAX_SPEED = 10.0;

    /**
     * Constant containing the filename of the default territory file.
     */
    private static final String DEFAULT_HAMSTER_TERRITORY = "/territories/example01.ter";

    /**
     * Game log object used to print log messages from the game and write commands from hamsters.
     */
    private final GameLog log = new GameLog();

    /**
     * Game command stack object used to execute game commands, i.e., commands comming from
     * the hamster objects on the territory during the simulation run.
     */
    private final GameCommandStack commandStack = new GameCommandStack();

    /**
     * The territory object on which the instance of this game takes place.
     */
    private final Territory territory = new Territory(this);

    /**
     * The input interface used when hamsters ask for input.
     */
    private InputInterface inputInterface;

    /**
     * Initialize a hamster game. The hamster game is in mode Initialized
     * in the beginning and needs to have its territory defined or loaded
     * and the game started to be useful.
     *
     * The default constructor initializes the hamster game with a dummy IO
     * interface, i.e., reading values is not allowed and exceptions from
     * the hamster game will be rethrown.
     */
    public HamsterGame() {
        this(new DummyInputInterface());
    }

    /**
     * Initialize a hamster game. The hamster game is in mode Initialized
     * in the beginning and needs to have its territory defined or loaded
     * and the game started to be useful.
     *
     * This constructor initializes the hamster game with the given
     * object to handle read commands of the hamster (e.g., from a GUI
     * or from Mockups) and to handle any exception thrown from the user
     * defined hamster game.
     *
     * @param newInputInterface The input interface this game should use
     * for reading values from the user and to handle exceptions.
     */
    public HamsterGame(final InputInterface newInputInterface) {
        super();
        this.inputInterface = newInputInterface;
    }

    /**
     * Set the speed of the hamster game. Valid values are in the range from 0.0 to 10.0,
     * where 0.0 is slow and 10.0 is fast.
     * @param gameSpeed The new game speed's delay. Has to be greater or equal 0.0 and
     *                  less than or equal 10.0.
     */
    public void setSpeed(final double gameSpeed) {
        Preconditions.checkArgument(gameSpeed >= 0.0 && gameSpeed <= MAX_SPEED);
        this.commandStack.setSpeed(gameSpeed);
    }

    /**
     * Getter for the territory object of this game. Cannot be null.
     * @return The territory object of this game.
     */
    public /*@ pure @*/ Territory getTerritory() {
        return territory;
    }

    /**
     * Getter for the game log. Cannot be null.
     * @return The game log of this hamster game's instance.
     */
    public GameLog getGameLog() {
        return log;
    }

    /**
     * Initialize a new hamster game by loading the default territory.
     */
    public void initialize() {
        try {
            initialize(DEFAULT_HAMSTER_TERRITORY);
        } catch (final IOException e) {
            throw new IllegalStateException("Unable to load default territory. "
                    + "This should not happen. Check jar for completeness.");
        }
    }

    /**
     * Initialize a new hamster game by loading the default territory.
     * @param territoryBuilder A territory builder which has been used before to
     *        build the territory.
     */
    public void initialize(final TerritoryBuilder territoryBuilder) {
        new EditCommandStack().execute(
                territoryBuilder.build());

    }

    /**
     * Initialize a new hamster game by loading the territory from the passed
     * territory file path.
     * @param territoryFile The territory file path. Has to be a location relative to
     *                      the classes' class path.
     * @throws IOException IOException occurs if the territory file could not be found or loaded.
     */
    public void initialize(final String territoryFile) throws IOException {
        new EditCommandStack().execute(
                TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromResourceFile(territoryFile));
    }

    /**
     * Initialize a new hamster game by loading the territory from the passed
     * territory file path.
     * @param inputStream The input stream to load the territory from.
     * @throws IOException IOException occurs if the territory file could not be found or loaded.
     */
    public void initialize(final InputStream inputStream) throws IOException {
        new EditCommandStack().execute(
                TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromInputStream(inputStream));
    }

    /**
     * Return a territory builder for this game's terriotry. The builder can be passed to an initialize call
     * later to use the created territory.
     * @return A territory builder for this territory
     */
    public TerritoryBuilder getNewTerritoryBuilder() {
        return TerritoryBuilder.getTerritoryBuilderForTerritory(getTerritory());
    }
    /**
     * Reset the hamster game to its initial state. Removes all hamsters besides  the
     * default hamster and places all gain objects to their initial position.
     */
    public void reset() {
        this.commandStack.undoAll();
        this.commandStack.reset();
    }

    /**
     * Gets the input interface of this game used to read values from users
     * or mock objects.
     * @return The input interface for this game.
     */
    public InputInterface getInputInterface() {
        checkState(this.inputInterface != null, "Input interface needs to be defined first!");
        return this.inputInterface;
    }

    /**
     * Sets this game's input interface for reading values from users or
     * mock objects.
     * @param newInputInterface The new input interface.
     */
    public void setInputInterface(final InputInterface newInputInterface) {
        checkNotNull(newInputInterface);
        this.inputInterface = newInputInterface;
    }

    /**
     * Opens a new Game UI for this game object. The game UI shows
     * the game and its current state while the game is executing.
     */
    public void displayInNewGameWindow() {
        JavaFXUI.openSceneFor(this.territory.getInternalTerritory(), this.commandStack, this.getGameLog());
    }

    /**
     * Run a given hamster program until it terminates. Termination
     * is either by ending the hamster game regularly or by throwing
     * an exception and not handling it inside the provided hamster program.
     *
     * The game will be started automatically if it had not been started before.
     * The game will be in paused mode, suitable for GUI runs, if it is started by
     * this method. After returning from this method the game will be in stopped state
     * no matter whether an exception was thrown or the program terminated regularly.
     *
     * Precondition to running the game is that the territory has been defined or loaded
     * and that an IO interface is attached for reading values and handling exceptions.
     *
     * @param hamsterProgram The hamster program to run.
     */
    public void runGame(final Consumer<Territory> hamsterProgram) {
        checkState(this.inputInterface != null, "Running a hamster game implies a defined IO interface first.");

        startGameIfNotStarted();
        try {
            hamsterProgram.accept(this.territory);
        } catch (final GameAbortedException e) {
        } catch (final RuntimeException e) {
            this.inputInterface.showAlert(e);
        } finally {
            stopGame();
        }
    }

    /**
     * Start the execution of a hamster game. Before executing start, no commands can be
     * executed by the hamster objects in the game.
     * @param startPaused if true the game will be started in pause mode
     */
    public void startGame(final boolean startPaused) {
        this.commandStack.startGame(startPaused);
    }

    /**
     * Stop the execution of the game. The game is finished and needs to be reseted
     * or closed.
     */
    public void stopGame() {
        this.commandStack.stopGame();
    }

    /**
     * Get the current state of this game.
     * @return The current state of this game.
     */
    public Mode getCurrentGameMode() {
        return this.commandStack.stateProperty().get();
    }

    /**
     * Start a hamster game, if it is not started yet.
     * The game will be started in paused mode, suited
     * for GUI interaction.
     */
    private void startGameIfNotStarted() {
        if (this.commandStack.stateProperty().get() == Mode.INITIALIZING
               || this.commandStack.stateProperty().get() == Mode.STOPPED) {
            startGame(true);
        }
    }

    /**
     * This is a central method of the hamster simulation engine. It implements the mediator pattern.
     * It accepts command specifications of game commands and distributes it to all game entities for
     * their partial execution. For example, each command specification is sent to the game log so that
     * it can create an appropriate log entry.
     * @param specification The command specification of the command to be executed in this game.
     */
    void processCommandSpecification(final CommandSpecification specification) {
        final Optional<Command> territoryCommandPart =
                territory.getInternalTerritory().getCommandFromSpecification(specification);
        final Optional<Command> logCommandPart = this.log.getCommandFromSpecification(specification);
        final Optional<Command> hamsterPart;
        if (specification instanceof AbstractHamsterCommandSpecification) {
            final AbstractHamsterCommandSpecification hamsterCommandSpec =
                    (AbstractHamsterCommandSpecification) specification;
            hamsterPart = hamsterCommandSpec.getHamster().getCommandFromSpecification(specification);
        } else {
            hamsterPart = Optional.empty();
        }
        final Command composite = new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                if (territoryCommandPart.isPresent()) {
                    builder.add(territoryCommandPart.get());
                }
                if (logCommandPart.isPresent()) {
                    builder.add(logCommandPart.get());
                }
                if (hamsterPart.isPresent()) {
                    builder.add(hamsterPart.get());
                }
            }
        };
        this.commandStack.execute(composite);
    }
}
