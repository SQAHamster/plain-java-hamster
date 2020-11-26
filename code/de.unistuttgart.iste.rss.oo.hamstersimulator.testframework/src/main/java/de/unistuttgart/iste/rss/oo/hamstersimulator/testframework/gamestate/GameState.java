package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Objects of this class store an immutable snapshot of the state
 * of a hamster game. The stored state consists of the information
 * on the number of grains on the territory tiles and the state
 * of each hamster on the territory.
 * @author Steffen Becker
 *
 */
public final class GameState implements Cloneable {

    /**
     * 2D-Array (row x column) capturing the grain count per tile. Does not treat wall tiles any special.
     */
    private final int[][] territoryGrainCount;

    /**
     * A map containing for all hamsters on the territory their current state.
     * Has to be a concrete hashmap for clone support.
     */
    private final HashMap<ObservableHamster, HamsterState> hamsterStates;

    /**
     * Optional reference to the previous game state. Is empty for the initial state.
     */
    private Optional<GameState> previousGameState = Optional.empty();

    /**
     * List of messages written by any of the hamsters.
     */
    private final ArrayList<WrittenMessage> writtenMessages;

    /**
     * Time stamp of this game state. It is counted in number of commands executed from the start
     * of the game onwards. The initial state has time stamp 0.
     */
    private final int timeStamp;

    GameState(final Size size) {
        this(size.getRowCount(), size.getColumnCount());
    }

    private GameState(final int rowCount, final int columnCount) {
        super();
        territoryGrainCount = new int[rowCount][columnCount];
        hamsterStates = new HashMap<>();
        writtenMessages = new ArrayList<>();
        timeStamp = 0;
    }

    private GameState(final GameState previousState) {
        super();
        territoryGrainCount = clone2dArray(previousState);
        hamsterStates = new HashMap<ObservableHamster, HamsterState>(previousState.getHamsterStates());
        writtenMessages = new ArrayList<WrittenMessage>(previousState.writtenMessages);
        previousGameState = Optional.of(previousState);
        timeStamp = previousState.timeStamp + 1;
    }

    /**
     * Query the number of grains on the tile with the given location.
     * @param location Location of the tile for which the number of grains is returned.
     * @return Number of grains on the tile at the given location.
     */
    public int grainCountAt(final Location location) {
        return getTerritoryGrainCount()[location.getRow()][location.getColumn()];
    }

    /**
     * Retrieve the state of the given hamster.
     * @param hamster The hamster whose state should be returned. Must not be null and
     *                must be a hamster on the territory.
     * @return The hamster state of the provided hamster.
     */
    public HamsterState getHamsterState(final ObservableHamster hamster) {
        Preconditions.checkState(getHamsterStates().containsKey(hamster),
                "The given hamster does not exist on the territory.");
        return getHamsterStates().get(hamster);
    }

    /**
     * @return whether this game state is the initial state of the game.
     */
    public boolean isInitialState() {
        return previousGameState.isEmpty();
    }

    /**
     * @return The predecessor of this game state.
     */
    public GameState getPreviousGameState() {
        Preconditions.checkState(!isInitialState());
        return previousGameState.get();
    }

    /**
     * Time stamp of this game state. It is counted in number of commands executed from the start
     * of the game onwards. The initial state has time stamp 0.
     * @return The time stamp of this game state.
     */
    public int getTimestamp() {
        return timeStamp;
    }

    /**
     * @return An immutable list of all the messages produced by write commands until this state.
     */
    public List<WrittenMessage> getMessageList() {
        return Collections.unmodifiableList(writtenMessages);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(territoryGrainCount);
        result = prime * result + Objects.hash(hamsterStates);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameState other = (GameState) obj;
        return Objects.equals(hamsterStates, other.hamsterStates)
                && Objects.equals(previousGameState, other.previousGameState)
                && Arrays.deepEquals(territoryGrainCount, other.territoryGrainCount);
    }

    /**
     * Create a new immutable copy of this game state.
     */
    @Override
    public GameState clone() {
        return new GameState(this);
    }

    int[][] getTerritoryGrainCount() {
        return territoryGrainCount;
    }

    HashMap<ObservableHamster, HamsterState> getHamsterStates() {
        return hamsterStates;
    }

    List<WrittenMessage> messageList() {
        return writtenMessages;
    }

    private int[][] clone2dArray(final GameState previousState) {
        return Arrays.stream(previousState.getTerritoryGrainCount()).map(int[]::clone).toArray(int[][]::new);
    }
}
