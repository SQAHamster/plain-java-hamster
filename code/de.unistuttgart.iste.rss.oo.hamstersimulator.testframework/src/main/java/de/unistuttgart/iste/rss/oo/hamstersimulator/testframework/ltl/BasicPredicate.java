package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import java.util.Collection;
import java.util.function.Predicate;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
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
public final class BasicPredicate implements LTLFormula {

    /**
     * The list of game states. The list is allowed to update. On updates
     * this predicate's collection of matching states is also updated.
     */
    private final ObservableList<GameState> allStates;

    /**
     * Public property containing all states which fulfill the provided basic logic predicate.
     */
    private final ReadOnlyListWrapper<GameState> matchingStates = new ReadOnlyListWrapper<>(this, "matchingStates",
            FXCollections.observableArrayList());

    /**
     * The basic logical condition which is tested against all known game states.
     */
    private final Predicate<GameState> condition;

    /**
     * Event handler to handle updated to the game state list.
     */
    private final ListChangeListener<GameState> updateMatchingStates =
            change -> {
                while (change.next()) {
                    BasicPredicate.this.updateMatchingStates(change.getAddedSubList());
                }
            };

    /**
     * Constructs a new basic predicate object and starts tracking the provided list of game states in order
     * to provide an up-to-date list of matching states.
     * @param gameStates List of game states. The list is allowed to update after the call to this constructor.
     * @param inclusionCondition The basic logic predicate which defines which states to include into the list of
     *                           matching game states
     */
    public BasicPredicate(final ObservableList<GameState> gameStates, final Predicate<GameState> inclusionCondition) {
        super();
        this.allStates = gameStates;
        this.condition = inclusionCondition;
        updateMatchingStates(allStates);
        this.allStates.addListener(updateMatchingStates);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        return matchingStates.contains(state);
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
