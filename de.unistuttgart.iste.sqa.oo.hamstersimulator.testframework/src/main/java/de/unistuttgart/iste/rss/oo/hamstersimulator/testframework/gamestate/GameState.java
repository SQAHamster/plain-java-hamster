package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * Objects of this class store an immutable snapshot of the state
 * of a hamster game. The stored state consists of the information
 * on the number of grains on the territory tiles and the state
 * of each hamster on the territory.
 * @author Steffen Becker
 *
 */
public final class GameState {

    /**
     * 2D-Array (row x column) capturing the grain count per tile. Does not treat wall tiles any special.
     */
    private final int[][] territoryGrainCount;

    /**
     * A map containing for all hamsters on the territory their current state.
     * Has to be an instance of hashmap for clone support.
     */
    private final HashMap<ObservableHamster, HamsterState> hamsterStates;

    /**
     * Optional reference to the previous game state. Is empty for the initial state.
     */
    private Optional<GameState> previousGameState = Optional.empty();

    /**
     * Optional reference to the next game state. Is empty for the final state.
     */
    private Optional<GameState> nextGameState = Optional.empty();

    /**
     * Optional reference to the command specification that was
     * executed as command and created this state. Is empty for the initial state.
     */
    private Optional<ObservableCommandSpecification> commandSpecification = Optional.empty();

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

    /**
     * Create a new game state based on the provided game state which will be used
     * as previous state. The new game state can only be changed by
     * the classes in this package. This copy is configured to become a valid successor
     * of the provided previous state. It is supposed to be further updated by the factory.
     * @param previousState The predecessor game state of the newly created game state
     */
    GameState(final GameState previousState) {
        super();

        Preconditions.checkNotNull(previousState);
        Preconditions.checkState(previousState.isFinalState());

        territoryGrainCount = clone2dArray(previousState);
        hamsterStates = new HashMap<ObservableHamster, HamsterState>(previousState.getHamsterStates());
        writtenMessages = new ArrayList<WrittenMessage>(previousState.writtenMessages);
        previousGameState = Optional.of(previousState);
        previousState.nextGameState = Optional.of(this);
        timeStamp = previousState.timeStamp + 1;
    }

    /**
     * Query the number of grains on the tile with the given location.
     * @param location Location of the tile for which the number of grains is returned.
     *                 Has to be null and has to be inside the territory.
     * @return Number of grains on the tile at the given location.
     */
    public int grainCountAt(final Location location) {
        Preconditions.checkNotNull(location);
        Preconditions.checkArgument(territoryGrainCount.length > location.getRow());
        Preconditions.checkArgument(territoryGrainCount[0].length > location.getColumn());
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
     * @return whether this game state is the final state of the game.
     */
    public boolean isFinalState() {
        return nextGameState.isEmpty();
    }

    /**
     * @return The predecessor of this game state.
     */
    public GameState getPreviousGameState() {
        Preconditions.checkState(!isInitialState());
        return previousGameState.get();
    }

    /**
     * @return The successor of this game state.
     */
    public GameState getNextGameState() {
        Preconditions.checkState(!isFinalState());
        return nextGameState.get();
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
     * @return the commandSpecification whose execution has lead to this new game state.
     *         Optional is empty for the initial state.
     */
    public Optional<ObservableCommandSpecification> getCommandSpecification() {
        return commandSpecification;
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
        result = prime * result + timeStamp;
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
                && Objects.equals(nextGameState, other.nextGameState) && Objects.equals(timeStamp, other.timeStamp)
                && Arrays.deepEquals(territoryGrainCount, other.territoryGrainCount);
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

    /**
     * @param newCommandSpecification the commandSpecification to set
     */
    void setCommandSpecification(final ObservableCommandSpecification newCommandSpecification) {
        this.commandSpecification = Optional.of(newCommandSpecification);
    }

    private int[][] clone2dArray(final GameState previousState) {
        return Arrays.stream(previousState.getTerritoryGrainCount()).map(int[]::clone).toArray(int[][]::new);
    }
}
