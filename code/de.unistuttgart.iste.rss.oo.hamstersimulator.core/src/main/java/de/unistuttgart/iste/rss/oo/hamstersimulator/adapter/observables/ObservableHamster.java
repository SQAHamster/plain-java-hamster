package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.function.Consumer;

/**
 * Interface to observe changes the internal hamster on a territory
 * Provides read only access to the hamster
 * It is not possible to find out how many grains a hamster has via this interface
 */
public interface ObservableHamster extends ObservableTileContent {

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
    Direction getDirection();

    /**
     * Getter for the direction property of the hamster, which represents
     * the direction this hamster is facing
     *
     * @return the property, not null
     */
    ReadOnlyObjectProperty<Direction> directionProperty();

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
    void addPickGrainListener(final Consumer<Location> listener);

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
    void removePickGrainListener(final Consumer<Location> listener);

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
    void addPutGrainListener(final Consumer<Location> listener);

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
    void removePutGrainListener(final Consumer<Location> listener);
}
