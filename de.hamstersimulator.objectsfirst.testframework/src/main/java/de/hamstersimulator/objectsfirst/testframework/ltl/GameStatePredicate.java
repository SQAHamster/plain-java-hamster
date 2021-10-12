package de.hamstersimulator.objectsfirst.testframework.ltl;

import java.util.Collection;
import java.util.function.Predicate;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * A basic predicate object represents a logical predicate which
 * will be checked against all states of the game. The basic predicate
 * tracks an up-to-date list of all states which match the basic logical
 * predicate. The basic logical predicate must not contain any temporal operators.
 * @author Steffen Becker
 */
public final class GameStatePredicate implements LTLFormula {

    /**
     * Message associated with this GameStatePredicate;
     */
    private final String message;

    /**
     * The list of game states. The list is allowed to update. On updates
     * this predicate's collection of matching states is also updated.
     */
    private final ObservableList<GameState> allStates;

    /**
     * Public property containing all states which fulfill the provided basic logic predicate.
     */
    private final ReadOnlyListWrapper<GameState> matchingStates =
            new ReadOnlyListWrapper<>(this, "matchingStates", FXCollections.observableArrayList());

    /**
     * The basic logical condition which is tested against all known game states.
     */
    private final Predicate<GameState> condition;

    /**
     * Event handler to handle updated to the game state list. This can only handle adding of states. Other
     * operations are not supported.
     */
    private final ListChangeListener<GameState> updateMatchingStatesListener = change -> {
        while (change.next()) {
            if (change.wasRemoved() || change.wasPermutated() || change.wasUpdated()
                    || change.wasReplaced()) {
                throw new UnsupportedOperationException(
                        "Game states should only be added, not altered in any other way.");
            }
            GameStatePredicate.this.updateMatchingStates(change.getAddedSubList());
        }
    };

    /**
     * Constructs a new basic predicate object and starts tracking the provided list of game states in order
     * to provide an up-to-date list of matching states.
     * @param gameStates List of game states. The list is allowed to update after the call to this constructor
     *        by adding new game states. Must not be null. Changes or removals are intentionally not supported.
     * @param inclusionCondition The basic logic predicate which defines which states to include into the list of
     *                           matching game states. Must not be null.
     * @param message A short description of the predicate.
     */
    public GameStatePredicate(final ObservableList<GameState> gameStates,
            final Predicate<GameState> inclusionCondition, final String message) {
        super();
        Preconditions.checkNotNull(gameStates);
        Preconditions.checkNotNull(inclusionCondition);
        Preconditions.checkNotNull(message);

        this.message = message;
        this.allStates = gameStates;
        this.condition = inclusionCondition;
        updateMatchingStates(allStates);
        this.allStates.addListener(updateMatchingStatesListener);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        Preconditions.checkArgument(allStates.contains(state));
        return matchingStates.contains(state);
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return An observable list of game states matching the provided basic logical predicate.
     * The list is updated when the game state list gets updated.
     */
    public ReadOnlyListProperty<GameState> getMatchingStates() {
        return matchingStates;
    }

    private void updateMatchingStates(final Collection<? extends GameState> statesToCheck) {
        for (final GameState state : statesToCheck) {
            if (condition.test(state)) {
                matchingStates.add(state);
            }
        }
    }
}
