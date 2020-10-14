package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.AbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;
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

    private final HamsterGameViewModel adapter;

    /**
     * Game log object used to print log messages from the game and write commands from hamsters.
     */
    private final GameLog log = new GameLog();

    /**
     * Game command stack object used to execute game commands, i.e., commands coming from
     * the hamster objects on the territory during the simulation run.
     */
    private final GameCommandStack commandStack = new GameCommandStack();

    /**
     * The territory object on which the instance of this game takes place.
     */
    private final Territory territory = new Territory(this);

    private final ExecutorService executorService;


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
        this(Collections.emptySet());
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
     * @param newInputInterfaces The input interfaces this game should use
     * for reading values from the user and to handle exceptions.
     */
    public HamsterGame(final Set<InputInterface> newInputInterfaces) {
        super();
        checkNotNull(newInputInterfaces);

        this.executorService = Executors.newCachedThreadPool(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        this.adapter = new HamsterGameViewModel(this.territory.getInternalTerritory(), this.commandStack, this.log,
                newInputInterfaces);
    }

    /*@
     @ requires gameSpeed >= 0 && gameSpeed <= MAX_SPEED;
     @ ensures getSpeed() == gameSpeed;
     @*/
    /**
     * Set the speed of the hamster game. Valid values are in the range from
     * 0.0 to 10.0,
     * where 0.0 is slow and 10.0 is fast.
     * @param gameSpeed The new game speed's delay. Has to be greater or equal 0.0 and
     *                  less than or equal 10.0.
     */
    public void setSpeed(final double gameSpeed) {
        Preconditions.checkArgument(gameSpeed >= 0.0 && gameSpeed <= MAX_SPEED);
        this.commandStack.setSpeed(gameSpeed);
    }

    /*@
     @ requires true;
     @ ensures gameSpeed >= 0 && gameSpeed <= MAX_SPEED;
     @*/
    /**
     * Gets the speed of the hamster game.
     * @return The hamster game's current speed. Is greater or equal 0.0 and
     *         less than or equal 10.0;
     */
    public double getSpeed() {
        return this.commandStack.speedProperty().get();
    }

    /**
     * Getter for the HamsterGameViewModel object of this game. Cannot be null.
     * The adapter can be used to connect a UI or a test framework to this game.
     * @return The adapter object of this game
     */
    public /*@ pure @*/ HamsterGameViewModel getModelViewAdapter() {
        return this.adapter;
    }

    /**
     * Getter for the territory object of this game. Cannot be null.
     * @return The territory object of this game.
     */
    public /*@ pure @*/ Territory getTerritory() {
        return territory;
    }

    /**
     * Initialize a new hamster game by loading the default territory.
     *
     * Warning: this executes a hard reset which cannot be undone
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
     *
     * Warning: this executes a hard reset which cannot be undone
     */
    public void initialize(final TerritoryBuilder territoryBuilder) {
        this.hardReset();

        new EditCommandStack().execute(territoryBuilder.build());
    }

    /**
     * Initialize a new hamster game by loading the territory from the passed
     * territory file path.
     * Warning: this executes a hard reset which cannot be undone
     *
     * @param territoryFile The territory file path. Has to be a location relative to
     *                      the classes' class path.
     * @throws IOException IOException occurs if the territory file could not be found or loaded.
     */
    public void initialize(final String territoryFile) throws IOException {
        this.hardReset();

        new EditCommandStack().execute(
                TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromResourceFile(territoryFile));
    }

    /**
     * Initialize a new hamster game by loading the territory from the passed
     * territory file path.
     * Warning: this executes a hard reset which cannot be undone
     *
     * @param inputStream The input stream to load the territory from.
     * @throws IOException IOException occurs if the territory file could not be found or loaded.
     */
    public void initialize(final InputStream inputStream) throws IOException {
        this.hardReset();

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

    /*@
     @ requires getCurrentGameMode() != Mode.INITIALIZING;
     @ ensures !this.commandStack.canUndoProperty().get();
     @ ensures (\old(getCurrentGameMode()) == Mode.RUNNING) ==> (getCurrentGameMode() == Mode.PAUSED);
     @ ensures (\old(getCurrentGameMode()) == Mode.PAUSED) ==> (getCurrentGameMode() == Mode.PAUSED);
     @ ensures (\old(getCurrentGameMode()) == Mode.STOPPED) ==> (getCurrentGameMode() == Mode.STOPPED);
     @*/
    /**
     * Soft-Reset the hamster game to its initial state (after started running). This undoes all steps. <br>
     * If the game was running or paused, the game is paused, however,
     * it is possible to execute further steps, which redoes all previous steps first.<br>
     * If the game is stopped, it also undoes all steps previous steps and it is possible to redo them,
     * but it is not possible to execute new steps. To do this, a hard reset is necessary. <br>
     * It is not possible to call this during initialisation.
     *
     * @throws IllegalStateException if getCurrentGameMode() == Mode.INITIALIZING
     */
    public void reset() {
        checkState(getCurrentGameMode() != Mode.INITIALIZING,
                "soft reset is not possible during initialization");

        final Mode currentMode = getCurrentGameMode();
        if (currentMode == Mode.RUNNING) {
            pauseGame();
            this.commandStack.undoAll();
        } else if (currentMode == Mode.PAUSED || currentMode == Mode.STOPPED) {
            this.commandStack.undoAll();
        }
    }

    /*@
     @ requires true;
     @ ensures !this.commandStack.canUndoProperty().get();
     @ ensures !this.commandStack.canRedoProperty().get();
     @ ensures getCurrentGameMode() == Mode.INITIALIZING;
     @*/
    /**
     * Hard-Reset the hamster game to its initial state (after started running). It is not possible to
     * repeat the simulation via redo. The mode is set to initializing, so it is possible to load another territory
     * file, however, it is necessary to call startGame again. <br>
     * This does not unload the currently loaded territory! <br>
     */
    public void hardReset() {
        this.commandStack.undoAll();
        this.commandStack.hardReset();
    }

    /**
     * Run a given hamster program until it terminates. Termination
     * is either by ending the hamster game regularly or by throwing
     * an exception and not handling it inside the provided hamster program.
     *
     * The game will be started automatically if it had not been started before.
     * The game will be in paused mode, suitable for GUI runs, if it is started by
     * this method. After returning from this method the game will be in stopped mode
     * no matter whether an exception was thrown or the program terminated regularly.
     *
     * Precondition to running the game is that the territory has been defined or loaded
     * and that an IO interface is attached for reading values and handling exceptions.
     *
     * @param hamsterProgram The hamster program to run.
     * @throws IllegalStateException if hamsterProgram is null or if no IO interface is defined
     */
    public void runGame(final Consumer<Territory> hamsterProgram) {
        checkNotNull(hamsterProgram);
        checkState(!this.adapter.getInputInterfaces().isEmpty(), "Running a hamster game implies a defined IO interface first.");

        startGameIfNotStarted();
        try {
            hamsterProgram.accept(this.territory);
        } catch (final GameAbortedException e) {
        } catch (final RuntimeException e) {
            this.showAlert(e);
        } finally {
            stopGame();
        }
    }

    /*@
     @ requires getCurrentGameMode() == Mode.INITIALIZING;
     @ ensures startPaused ==> getCurrentGameMode() == Mode.PAUSED;
     @ ensures !startPaused ==> getCurrentGameMode() == Mode.RUNNING;
     @*/
    /**
     * Start the execution of a hamster game. Before executing start, no commands can be
     * executed by the hamster objects in the game.
     * This is only possible if the current mode is Mode.INITIALIZING
     * @param startPaused if true the game will be started in pause mode
     * @throws IllegalStateException if getCurrentGameMode() != Mode.INITIALIZING
     */
    public void startGame(final boolean startPaused) {
        checkState(getCurrentGameMode() == Mode.INITIALIZING,
                "start game is only possible during initialization");

        this.commandStack.startGame(startPaused);
    }

    /*@
     @ requires true;
     @ ensures getCurrentGameMode() == Mode.STOPPED;
     */
    /**
     * Stop the execution of the game. The game is finished and needs to be reset / hardReset
     * or closed.
     * If the game is already stopped, this does nothing
     */
    public void stopGame() {
        this.commandStack.stopGame();
    }

    /*@
     @ requires getCurrentGameMode() == Mode.RUNNING;
     @ ensures getCurrentGameMode() == Mode.PAUSED;
     @*/
    /**
     * Pauses the HamsterGame.
     * It is only possible to execute this in running mode.
     *
     * @throws IllegalStateException if getCurrentGameMode() != Mode.RUNNING
     */
    public void pauseGame() {
        checkState(getCurrentGameMode() == Mode.RUNNING, "pauseGame is only possible in running mode");

        this.commandStack.pause();
    }

    /*@
     @ requires getCurrentGameMode() == Mode.PAUSED;
     @ ensures getCurrentGameMode() == Mode.RUNNING;
     @*/
    /**
     * Pauses the HamsterGame.
     * It is only possible to execute this in paused mode.
     *
     * @throws IllegalStateException if getCurrentGameMode() != Mode.PAUSED
     */
    public void resumeGame() {
        checkState(getCurrentGameMode() == Mode.PAUSED, "resumeGame is only possible in paused mode");

        this.commandStack.resume();
    }

    /*@
     @ requires true;
     @ pure;
     @ ensures \result != null;
     @*/
    /**
     * Get the current mode of this game.
     * @return The current mode of this game.
     */
    public Mode getCurrentGameMode() {
        return this.commandStack.modeProperty().get();
    }

    /*@
     @ requires true;
     @ ensures ((\old(getCurrentGameMode()) == MODE.INITIALIZING) || (\old(getCurrentGameMode()) == MODE.STOPPED))
     @          ==> (getCurrentGameMode() == Mode.PAUSED);
     @*/
    /**
     * Start a hamster game, if it is not started yet or if it is stopped.
     * The game will be started in paused mode, suited
     * for GUI interaction. <br>
     * Note: if the game is stopped, a hard reset is performed before restarting
     * Note: If the game is already started and running, it is not paused
     */
    private void startGameIfNotStarted() {
        if (getCurrentGameMode() == Mode.STOPPED) {
            this.hardReset();
            this.startGame(true);
        } else if (getCurrentGameMode() == Mode.INITIALIZING) {
            this.startGame(true);
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

    /**
     * Inform a user about an abnormal execution aborting.
     * This blocks until it returns or is aborted
     * @param t The throwable which lead to aborting the program.
     */
    public void showAlert(final Throwable t) {
        final Throwable res = this.executeAndGetFirstResult(inputInterface -> () -> {
            try {
                inputInterface.showAlert(t);
            } catch (Exception e) {
                return Optional.of(e);
            }
            return Optional.empty();
        });

        if (res != null) {
            if (res instanceof RuntimeException) {
                throw (RuntimeException)res;
            } else {
                throw new RuntimeException(res);
            }
        }
    }

    /**
     * Read an integer value from a user. This blocks until there is
     * an integer to return or it is aborted
     * @param message The message used in the prompt for the number.
     * @return The integer value read or an empty optional, if aborted.
     */
    protected int readInteger(final String message) {
        return this.executeAndGetFirstResult(inputInterface -> () -> inputInterface.readInteger(message));
    }

    /**
     * Read a string value from a user. This blocks until there is a
     * String to return or it is aborted
     * @param message The message used in the prompt for the string.
     * @return The string value read or an empty optional, if aborted.
     */
    protected String readString(final String message) {
        return this.executeAndGetFirstResult(inputInterface -> () -> inputInterface.readString(message));
    }

    /**
     * executes the callable with every input interface in parallel
     * @param callableFactory factory to create the Callable out of the InputInterface
     * @param <R> the return type
     * @return the result of the first callable that completes
     */
    private <R> R executeAndGetFirstResult(final Function<InputInterface, Callable<Optional<R>>> callableFactory) {
        final CompletionService<Optional<R>> completionService = submitInputRequests(callableFactory);

        try {
            return getFirstResult(completionService);
        } finally {
            abortInputRequests(completionService);
        }
    }

    /**
     * Creates a new CompletionService and submits a callable to each input interface to handle the input request
     * @param callableFactory creates the callable for the CompletionService on invocation
     * @param <R> the return type of the input request
     * @return the CompletionService which handes all input requests
     */
    private <R> CompletionService<Optional<R>> submitInputRequests(final Function<InputInterface, Callable<Optional<R>>> callableFactory) {
        final CompletionService<Optional<R>> completionService = new ExecutorCompletionService<>(this.executorService);
        for (final InputInterface inputInterface : this.adapter.getInputInterfaces()) {
            completionService.submit(callableFactory.apply(inputInterface));
        }
        return completionService;
    }

    /**
     * Takes th first result from the completionService and return its result if possible,
     * otherwise throws an exception
     * @param completionService the service which handles all input requests
     * @param <R> the return type of the input request
     * @return the first result if possible
     * @throws IllegalStateException if nothing was returned from the first request which returned or an
     *                               internal error occurs
     */
    private <R> R getFirstResult(final CompletionService<Optional<R>> completionService) {
        try {
            final Optional<R> result = completionService.take().get();
            if (result.isPresent()) {
                return result.get();
            } else {
                throw new IllegalStateException("nothing returned");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("interrupted");
        } catch (ExecutionException e) {
            throw new RuntimeException("nothing returned", e);
        }
    }

    /**
     * Aborts all remaining input requests by calling abort on every input interface
     * @param completionService the service which handles all input requests
     * @param <R> the return type of the input request
     */
    private <R> void abortInputRequests(final CompletionService<Optional<R>> completionService) {
        for (final InputInterface inputInterface : this.adapter.getInputInterfaces()) {
            inputInterface.abort();
        }
        for (int i = 0; i < this.adapter.getInputInterfaces().size() - 1; i++) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }

}