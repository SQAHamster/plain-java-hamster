package de.hamstersimulator.objectsfirst.testframework.gamestate;

import de.hamstersimulator.objectsfirst.testframework.gamelog.GameLogFactory;
import de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes.LogEntry;
import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableLogEntry;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTerritory;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.testframework.HamsterGameTestEnvironment;
import de.hamstersimulator.objectsfirst.utils.Preconditions;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

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

    private final GameLogFactory gameLogFactory;

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

    public void setErrorMessage(final GameState state, final String errorMessage) {
        this.stateLogEntryLookup.get(state).setErrorMessage(errorMessage);
    }

    public String getLogJson() {
        final Gson gson = new Gson();
        return gson.toJson(this.gameLogFactory.toGameLog());
    }

}
