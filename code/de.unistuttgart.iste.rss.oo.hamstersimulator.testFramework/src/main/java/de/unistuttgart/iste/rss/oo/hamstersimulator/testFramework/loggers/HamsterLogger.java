package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events.*;
import javafx.beans.value.ObservableValue;

import java.util.*;

/*@ instance invariant this.hamster != null && events != null;
 */

/**
 * A class which logs all actions of a specific hamster and provides a log after the game ended
 * <p>
 * The class takes an observable hamster and registers all needed event listeners needed for logging the hamsters actions.
 * The following hamster actions are logged:
 * <ul>
 *     <li>
 *         Move (as {@link MoveEvent} or if more than one tile in one step: {@link MultiMoveEvent})
 *         <small>Note: Multiple single moves are NOT combined into one SingleMoveEvent</small>
 *     </li>
 *     <li>Turn (as {@link TurnLeftEvent} if Hamster turns one step to the left or {@link TurnEvent} else)</li>
 *     <li>Placing and removing the hamster from a territory ({@link PlacedOnTerritoryEvent}/{@link RemovedFromTerritoryEvent})</li>
 *     <li>Putting a grain onto the tile ({@link PutGrainEvent})</li>
 *     <li>Picking a grain from the tile ({@link PickGrainEvent})</li>
 * </ul>
 */
public class HamsterLogger {

    /**
     * The hamster the actions should be logged of
     */
    private final ObservableHamster hamster;

    /**
     * List of all {@link HamsterEvent}s that were already logged
     */
    private final List<HamsterEvent> events;

    /*@ requires hamster != null;
     */

    /**
     * Creates a new {@link HamsterLogger} for the given hamster from the point when the logger was created.
     * The logger will start logging immediately after being created.
     * It will register event listeners for all needed properties
     * <p>
     * NOTE: No Events that occurred before creating the logger are logged
     *
     * @param hamster The hamster of which to log the actions. This can't be null
     */
    public HamsterLogger(final ObservableHamster hamster) {
        if (hamster == null) {
            throw new IllegalArgumentException("The hamster to log for can't be null");
        }
        this.hamster = hamster;
        this.events = new ArrayList<>();
        hamster.currentTileProperty().addListener(this::locationUpdated);
        hamster.directionProperty().addListener(this::directionUpdated);
        hamster.addPutGrainListener(this::handlePutGrain);
        hamster.addPickGrainListener(this::handlePickGrain);
    }

    /*@ requires true;
      @ ensures \result != null;
     */

    /**
     * Returns an unmodifiable copy of the event log
     * <p>
     * All event-instances that were logged from the hamster for one of the actions will be contained in this log list
     *
     * @return A non null, unmodifiable list containing all logged event-instances ({@link Collections#unmodifiableList(List)}
     */
    public /*@pure*/ List<HamsterEvent> getLog() {
        return Collections.unmodifiableList(this.events);
    }

    /**
     * Event handler for the updating of the position of the hamster
     * <p>
     * The event handler processes the previous and new location and handles three cases:
     * <ol>
     *     <li>
     *         The old tile is not present but the new one is:<br>
     *         The hamster was placed on a new territory ({@link PlacedOnTerritoryEvent})
     *     </li>
     *     <li>
     *         Both old and new tile are present:<br>
     *         The hamster has moved within the territory<br>
     *         If it has moved one tile horizontally or vertically {@link MoveEvent} is logged<br>
     *         If it has moved in any other way a {@link MultiMoveEvent} is logged
     *     </li>
     *     <li>
     *         The old tile is present but the new isn't:<br>
     *             The hamster was removed from the territory ({@link RemovedFromTerritoryEvent})
     *     </li>
     * </ol>
     *
     * @param observable The observable that generated the event. This isn't used
     * @param oldValue   The old tile, the hamster was on before it was moved. If empty, it means the hamster was not on a territory<br>
     *                   This can't be null.
     * @param newValue   The new/current tile, the hamster is now on after it was moved. If empty, it means the hamster is not on a territory anymore<br>
     *                   This can't be null.
     */
    private void locationUpdated(final ObservableValue<? extends Optional<? extends ObservableTile>> observable, final Optional<? extends ObservableTile> oldValue, final Optional<? extends ObservableTile> newValue) {
        assert oldValue != null;
        assert newValue != null;

        if (oldValue.isEmpty() && newValue.isPresent()) {
            this.events.add(new PlacedOnTerritoryEvent((new Date()).getTime(), newValue.get().getLocation()));
        } else if (oldValue.isPresent() && newValue.isPresent()) {
            final Location start = oldValue.get().getLocation();
            final Location end = newValue.get().getLocation();
            if (Math.abs(end.getColumn() - start.getColumn()) + Math.abs(end.getRow() - start.getRow()) == 1) {
                this.events.add(new MoveEvent((new Date()).getTime(), oldValue.get(), newValue.get()));
            } else {
                this.events.add(new MultiMoveEvent((new Date()).getTime(), oldValue.get(), newValue.get()));
            }
        } else if (oldValue.isPresent() && newValue.isEmpty()) {
            this.events.add(new RemovedFromTerritoryEvent((new Date()).getTime(), oldValue.get().getLocation()));
        }
    }

    /**
     * Event handler for the updating of the direction of the hamster
     * <p>
     * The event handler processes the previous and new direction the hamster faces.
     * Only if the hamster is on a territory (has a current tile) the direction change event will be logged.
     * If the hamster was turned exactly one step to the left, a {@link TurnLeftEvent} will be logged, else a {@link TurnEvent}
     *
     * @param observable The observable that generated the event. This isn't used
     * @param oldValue   The old direction, the hamster was facing before it was turned.<br>
     *                   This can't be null and must be a valid entry of the {@link Direction} enum.
     * @param newValue   The new/current direction, the hamster is now facing after it was turned.<br>
     *                   This can't be null and must be a valid entry of the {@link Direction} enum.
     */
    private void directionUpdated(final ObservableValue<? extends Direction> observable, final Direction oldValue, final Direction newValue) {
        assert oldValue != null;
        assert newValue != null;

        if (this.hamster.getCurrentLocation().isPresent()) {
            if (newValue.equals(oldValue.left())) {
                this.events.add(new TurnLeftEvent((new Date()).getTime(), this.hamster.getCurrentLocation().get(), newValue));
            } else {
                this.events.add(new TurnEvent((new Date()).getTime(), this.hamster.getCurrentLocation().get(), oldValue, newValue));
            }
        }
    }

    /**
     * Event handler for the put grain event of the hamster
     * <p>
     * This method is invoked by the hamster once a grain is put down from the hamster's mouth onto the territory.
     * The location the hamster was on while placing the grain will be documented.
     * <p>
     * For each grain that is placed by the hamster a new {@link PutGrainEvent} event will be logged
     *
     * @param location The location of the tile the hamster placed the grain on. This can't be null
     */
    private void handlePutGrain(final Location location) {
        assert location != null;

        this.events.add(new PutGrainEvent((new Date()).getTime(), location));
    }

    /**
     * Event handler for the pick grain event of the hamster
     * <p>
     * This method is invoked by the hamster once a grain is picked up from the territory into the hamster's mouth.
     * The location the hamster was on while picking the grain will be documented.
     * <p>
     * For each grain that is picked up by the hamster a new event {@link PickGrainEvent} will be logged
     *
     * @param location The location of the tile the hamster picked the grain from. This can't be null
     */
    private void handlePickGrain(final Location location) {
        assert location != null;

        this.events.add(new PickGrainEvent((new Date()).getTime(), location));
    }


}
