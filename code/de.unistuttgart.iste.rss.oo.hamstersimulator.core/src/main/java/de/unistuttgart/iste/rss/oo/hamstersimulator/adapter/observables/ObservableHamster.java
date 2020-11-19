package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import javafx.beans.property.ReadOnlyObjectProperty;

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
    
}
