package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TileContent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ReadOnlyHamster extends TileContent implements ObservableHamster {

    final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<Direction>(this, "direction", Direction.NORTH);
    final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<Grain>(this, "grainInMouth", FXCollections.observableArrayList());
    final ReadOnlyIntegerWrapper grainCount = new ReadOnlyIntegerWrapper(this, "grainCount", 0);
    final Set<Consumer<Location>> pickGrainHandlers = new HashSet<>();
    final Set<Consumer<Location>> putGrainHandlers = new HashSet<>();

    public ReadOnlyHamster() {
        super();
        this.grainCount.bind(this.grainInMouth.sizeProperty());
        this.grainInMouth.addListener((ListChangeListener<Grain>) changes -> {
            if (this.getCurrentLocation().isPresent()) {
                while (changes.next()) {
                    changes.getAddedSubList().forEach(grain -> this.pickGrainHandlers.forEach(handler -> handler.accept(this.getCurrentLocation().get())));
                    changes.getRemoved().forEach(grain -> this.putGrainHandlers.forEach(handler -> handler.accept(this.getCurrentLocation().get())));
                }
            }
        });
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     @*/

    /**
     * Get the current hamster looking direction.
     *
     * @return The current hamster's looking direction.
     */
    @Override
    public Direction getDirection() {
        return this.direction.get();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(this.grainInMouth.get());
    }

    public /*@ pure helper @*/ int getGrainCount() {
        return this.grainCount.get();
    }

    /**
     * Getter for the direction property of the hamster, which represents
     * the direction this hamster is facing
     *
     * @return the property, not null
     */
    @Override
    public ReadOnlyObjectProperty<Direction> directionProperty() {
        return this.direction.getReadOnlyProperty();
    }

    /*@ requires listener != null;
      @ ensures this.pickGrainHandlers.contains(listener);
     */

    /**
     * Adds a new listener on the pick-grain event.
     * <p>
     * The event will be invoked after the hamster picks up a grain from the territory
     * Duplicate handlers will only be added and called once
     *
     * @param listener The listener for the pick-grain event (This consumes the location the hamster picked the grain from)
     *                 This can't be null
     */
    @Override
    public void addPickGrainListener(final Consumer<Location> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener to be added to the pick-grain event can't be null");
        }
        this.pickGrainHandlers.add(listener);
    }

    /*@ requires listener != null;
      @ ensures !this.pickGrainHandlers.contains(listener);
     */

    /**
     * Removed the given listener from the pick-grain event
     * <p>
     * The event will no longer be invoked.
     * If the event wasn't previously added to the event, nothing will happen
     *
     * @param listener The listener to remove
     *                 This can't be null
     */
    @Override
    public void removePickGrainListener(final Consumer<Location> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener to be removed from the pick-grain event can't be null");
        }
        this.pickGrainHandlers.remove(listener);
    }

    /*@ requires listener != null;
      @ ensures this.putGrainHandlers.contains(listener);
     */

    /**
     * Adds a new listener on the put-grain event.
     * <p>
     * The event will be invoked after the hamster puts a grain onto the territory
     * Duplicate handlers will only be added and called once
     *
     * @param listener The listener for the put-grain event (This consumes the location the hamster puts the grain onto)
     *                 This can't be null
     */
    @Override
    public void addPutGrainListener(final Consumer<Location> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener to be added to the put-grain event can't be null");
        }
        this.putGrainHandlers.add(listener);
    }

    /*@ requires listener != null;
      @ ensures !this.putGrainHandlers.contains(listener);
     */

    /**
     * Removed the given listener from the put-grain event
     * <p>
     * The event will no longer be invoked.
     * If the event wasn't previously added to the event, nothing will happen
     *
     * @param listener The listener to remove
     *                 This can't be null
     */
    @Override
    public void removePutGrainListener(final Consumer<Location> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener to be removed from the put-grain event can't be null");
        }
        this.putGrainHandlers.remove(listener);
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty grainCountProperty() {
        return this.grainCount.getReadOnlyProperty();
    }

    public ReadOnlyTerritory getCurrentTerritory() {
        if (!this.getCurrentTile().isPresent()) {
            throw new IllegalStateException();
        }
        return this.getCurrentTile().get().getTerritory();
    }

    /*
     * OO-Design Methods
     */
    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}