package de.hamstersimulator.objectsfirst.testframework.gamestate;

import de.hamstersimulator.objectsfirst.testframework.gamelog.GameLogException;
import de.hamstersimulator.objectsfirst.testframework.gamelog.GameLogFactory;
import de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes.GameLog;
import de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes.LogEntry;
import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableLogEntry;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTerritory;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.testframework.HamsterGameTestEnvironment;
import de.hamstersimulator.objectsfirst.testframework.ltl.LTLFormula;
import de.hamstersimulator.objectsfirst.testframework.ltl.StateCheckException;
import de.hamstersimulator.objectsfirst.utils.Preconditions;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class extends {@link HamsterGameTestEnvironment} by the functionality to
 * record all states of the game while it is running.
 * @author Steffen Becker
 *
 */
public class RecordingHamsterGameTestEnvironment extends HamsterGameTestEnvironment {

    /**
     * List used to store the sequence of game states of the executed game.
     */
    private final ReadOnlyListWrapper<GameState> gameStates = new ReadOnlyListWrapper<>(
            FXCollections.observableArrayList());

    /**
     * A lambda containing the event handler called when a new log entry is added to the game log.
     */
    private final ListChangeListener<? super ObservableLogEntry> commandAdditionHandler =
            change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (final ObservableLogEntry newLog : change.getAddedSubList()) {
                            processNewLog(newLog.getCommandSpecification());
                        }
                }
            }
            };

    /**
     * Factory used to create the GameLog
     */
    private final GameLogFactory gameLogFactory;

    /**
     * Associates a LogEntry with each GameState
     * Used to set error messages for log entries
     */
    private final Map<GameState, LogEntry> stateLogEntryLookup = new HashMap<>();

    /**
     * Constructs a new recording environment. This environment encapsulates a simple hamster
     * game. This game is observed for all commands executed. For each command, a new
     * game state is created capturing the state of the game after the command's execution.
     * @param targetGame The game to be executed and observed.
     */
    public RecordingHamsterGameTestEnvironment(final SimpleHamsterGame targetGame) {
        super(targetGame);
        Preconditions.checkNotNull(targetGame);

        final HamsterGameViewModel viewModel = this.getViewModel();
        final ObservableTerritory territory = viewModel.getTerritory();
        viewModel.getLog().logProperty().addListener(commandAdditionHandler);

        final GameState initialState = GameStateFactory.newGameStateFactory().fromInitialTerritory(territory);
        gameStates.add(initialState);
        this.gameLogFactory = new GameLogFactory(territory);
    }

    /**
     * @return An observable list of the sequence of game states.
     */
    public ReadOnlyListProperty<GameState> getGameStates() {
        return gameStates;
    }

    /**
     * Called for each new game log entry. Creates and memorizes a new game state. Triggers an
     * update of the observable game state list.
     *
     * @param observableCommandSpecification The specification of the command which has been executed.
     */
    private void processNewLog(final ObservableCommandSpecification observableCommandSpecification) {
        assert observableCommandSpecification != null;

        final GameState nextState = GameStateFactory.newGameStateFactory()
                .cloneFromPreviousState(gameStates.get(gameStates.size() - 1))
                .constructNextState(observableCommandSpecification);
        final LogEntry logEntry = this.gameLogFactory.applyNextCommand(observableCommandSpecification);
        gameStates.add(nextState);
        this.stateLogEntryLookup.put(nextState, logEntry);
    }

    /**
     * Sets the error message associated with a specific GameState
     * The message is added to the LogEntry
     *
     * @param state the state which caused the error
     * @param errorMessage the error message
     */
    public void setErrorMessage(final GameState state, final String errorMessage) {
        this.stateLogEntryLookup.get(state).setErrorMessage(errorMessage);
    }

    /**
     * Creates the GameLog and returns it
     * @return the created GameLog
     */
    public GameLog getGameLog() {
        return this.gameLogFactory.toGameLog();
    }

    /**
     * @param formula   The ltl formula to check against.
     * @param gameState The game state against which the ltl formula is checked.
     * @param message   The user defined message which should describe the intention of the
     *                  ltl formula in natural language.
     *
     * Helper method for writing JUnit tests. The given formula is checked
     * against the given game state. If this check is true, the method returns. Otherwise, a
     * new {@link GameLogException} with a new {@link StateCheckException} as cause is generated and thrown.
     * Also, automatically sets the error message for the LogEntry which caused the failure.
     */
    public void checkOrThrowWithLog(final LTLFormula formula, final GameState gameState, final String message) {
        final Optional<GameState> failedAtState = formula.failsAt(gameState);
        if (failedAtState.isPresent()) {
            this.setErrorMessage(failedAtState.get(), message);
            final StateCheckException exception = StateCheckException.createStateCheckException(formula, gameState, message);
            throw new GameLogException(exception, this.getGameLog());
        }
    }

    /**
     * Runs the underlying HamsterGame by calling doRun on the
     * SimpleHamsterGame
     * Warning: it is possible to call this multiple times, and each time doRun is called on the
     * SimpleHamsterGame. This might cause strange behavior and therefore is highly discouraged.
     * This wraps all thrown Exceptions so that a GameLog is produced
     *
     * @throws GameLogException in case of any exception
     */
    @Override
    public void runGame() {
        try {
            super.runGame();
        } catch (final Exception exception) {
            final GameLog gameLog = this.getGameLog();
            final List<LogEntry> logEntries = gameLog.logEntries();
            final LogEntry lastLogEntry = logEntries.get(logEntries.size() - 1);
            lastLogEntry.setErrorMessage(exception.getMessage());
            throw new GameLogException(exception, gameLog);
        }
    }
}
