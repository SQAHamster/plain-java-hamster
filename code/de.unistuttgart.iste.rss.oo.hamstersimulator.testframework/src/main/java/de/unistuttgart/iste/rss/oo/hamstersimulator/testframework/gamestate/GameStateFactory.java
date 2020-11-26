package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import java.util.HashMap;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableInitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableWriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Internal factory class used to create new game states. Game states can
 * be created from an initial territory or from a previous state and the command
 * specification executed on that state.
 * @author Steffen Becker
 *
 */
final class GameStateFactory {

    /**
     * Delta of the grains when exactly one grain is picked up.
     */
    private static final GrainDelta PICK_GRAIN_DELTA = new GrainDelta(1, 0);

    /**
     * Delta of the grains when exactly one grain is put on the territory.
     */
    private static final GrainDelta PUT_GRAIN_DELTA = new GrainDelta(0, 1);

    /**
     * Number of grains removed from a tile on a pick command.
     */
    private static final int GRAIN_PICKED = -1;

    /**
     * Number of grains added to a tile on a put command.
     */
    private static final int GRAIN_DROPPED = 1;

    /**
     * The newly created next game state. Used to store intermediate phases
     * during the construction process.
     */
    private Optional<GameState> constructedState = Optional.empty();

    /**
     * Dispatcher used to execute state change logic dependant on the command
     * specification executed on the previous state. The return value is not used here.
     */
    private final LambdaVisitor<ObservableCommandSpecification, Void> logVisitor;

    /**
     * @return A new game state factory. The factory is not initialized and requires
     * additional commands to configure the game state to construct.
     */
    public static GameStateFactory newGameStateFactory() {
        return new GameStateFactory();
    }

    /**
     * Configures the game state to construct based on the previous state.
     * It clones the previous state and remembers it for further modifications.
     * @param previousState The previous game state preceding the state to be constructed.
     * @return This game state factory for fluent API use.
     */
    public GameStateFactory cloneFromPreviousState(final GameState previousState) {
        Preconditions.checkNotNull(previousState);
        Preconditions.checkState(this.constructedState.isEmpty());
        this.constructedState = Optional.of((GameState) previousState.clone());
        return this;
    }

    /**
     * Construct the next game state and return it. Requires that the previous state has been
     * configured before.
     * @param commandSpecification Specification of the command to apply to the previous state.
     * @return The constructed next state.
     */
    public GameState constructNextState(final ObservableCommandSpecification commandSpecification) {
        this.logVisitor.apply(commandSpecification);
        return constructedState.get();
    }

    /**
     * Construct an initial game state from the state of the passed territory. It is assumed
     * that the given territory is in the state right after its initialization (i.e., only the default
     * hamster exists).
     * @param observableTerritory The territory used to create the initial state from.
     * @return The newly constructed initial state.
     */
    public GameState fromInitialTerritory(final ObservableTerritory observableTerritory) {
        Preconditions.checkNotNull(observableTerritory);
        Preconditions.checkState(observableTerritory.getHamsters().size() == 1);
        Preconditions.checkState(
                observableTerritory.getSize().getColumnCount() > 0 && observableTerritory.getSize().getRowCount() > 0);

        final GameState result = new GameState(observableTerritory.getSize());
        final ObservableHamster defaultHamster = observableTerritory.getDefaultHamster();
        final HamsterState defaultHamsterState = new HamsterState(defaultHamster.getDirection(),
                defaultHamster.getCurrentLocation().get(), 0, 0);
        result.getHamsterStates().put(defaultHamster, defaultHamsterState);
        for (final ObservableTile tile : observableTerritory.tilesProperty()) {
            result.getTerritoryGrainCount()[tile.getLocation().getRow()][tile.getLocation().getColumn()] = tile
                    .getGrainCount();
        }
        return result;
    }

    private GameStateFactory() {
        super();
        this.logVisitor = new LambdaVisitor<ObservableCommandSpecification, Void>()
                .on(ObservableMoveCommandSpecification.class).then(spec -> fromMove(spec))
                .on(ObservableTurnLeftCommandSpecification.class).then(spec -> fromTurnLeft(spec))
                .on(ObservablePutGrainCommandSpecification.class).then(spec -> fromPutGrain(spec))
                .on(ObservablePickGrainCommandSpecification.class).then(spec -> fromPickGrain(spec))
                .on(ObservableInitHamsterCommandSpecification.class).then(spec -> fromInit(spec))
                .on(ObservableWriteCommandSpecification.class).then(spec -> fromWrite(spec));
    }

    private Void fromWrite(final ObservableWriteCommandSpecification writeCommandSpecification) {
        final GameState gameStateToUpdate = this.constructedState.get();
        final WrittenMessage newWrittenMessage = new WrittenMessage(writeCommandSpecification.getMessage(),
                writeCommandSpecification.getHamster());
        gameStateToUpdate.messageList().add(newWrittenMessage);
        return null;
    }

    private Void fromInit(final ObservableInitHamsterCommandSpecification initCommandSpecification) {
        final GameState gameStateToUpdate = this.constructedState.get();
        final HashMap<ObservableHamster, HamsterState> hamsterMapToUpdate = gameStateToUpdate.getHamsterStates();
        final ObservableHamster hamsterToUpdate = initCommandSpecification.getHamster();
        final HamsterState newHamsterState = new HamsterState(hamsterToUpdate.getDirection(),
                hamsterToUpdate.getCurrentLocation().get(), 0, 0);
        hamsterMapToUpdate.put(hamsterToUpdate, newHamsterState);

        return null;
    }

    private Void fromMove(final ObservableMoveCommandSpecification moveCommandSpecification) {
        updateHamsterState(moveCommandSpecification, GrainDelta.NO_CHANGE);
        return null;
    }

    private Void fromTurnLeft(final ObservableTurnLeftCommandSpecification turnCommandSpecification) {
        updateHamsterState(turnCommandSpecification, GrainDelta.NO_CHANGE);
        return null;
    }

    private Void fromPutGrain(final ObservablePutGrainCommandSpecification putCommandSpecification) {
        updateHamsterState(putCommandSpecification, PUT_GRAIN_DELTA);
        updateGrainCountAt(putCommandSpecification.getHamster().getCurrentLocation().get(), GRAIN_DROPPED);
        return null;
    }

    private Void fromPickGrain(final ObservablePickGrainCommandSpecification pickCommandSpecification) {
        updateHamsterState(pickCommandSpecification, PICK_GRAIN_DELTA);
        updateGrainCountAt(pickCommandSpecification.getHamster().getCurrentLocation().get(), GRAIN_PICKED);
        return null;
    }

    private void updateGrainCountAt(final Location location, final int delta) {
        final int[][] territory = this.constructedState.get().getTerritoryGrainCount();
        territory[location.getRow()][location.getColumn()] += delta;
    }

    private void updateHamsterState(final ObservableAbstractHamsterCommandSpecification hamsterCommandSpecification,
            final GrainDelta newDelta) {
        final GameState gameStateToUpdate = this.constructedState.get();
        final HashMap<ObservableHamster, HamsterState> hamsterMapToUpdate = gameStateToUpdate.getHamsterStates();
        final ObservableHamster hamsterToUpdate = hamsterCommandSpecification.getHamster();
        final HamsterState hamsterStateToUpdate = hamsterMapToUpdate.get(hamsterToUpdate);
        final HamsterState newHamsterState = new HamsterState(hamsterToUpdate.getDirection(),
                hamsterToUpdate.getCurrentLocation().get(),
                hamsterStateToUpdate.getGrainCollected() + newDelta.getDeltaPickedGrains(),
                hamsterStateToUpdate.getGrainDropped() + newDelta.getDeltaDroppedGrains());
        hamsterMapToUpdate.put(hamsterToUpdate, newHamsterState);
    }
}
