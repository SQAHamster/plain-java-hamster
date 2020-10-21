package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TileContent;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;

import java.util.List;

/**
 * Interface to observe changes on a tile of a territory
 * Provides access to its contents, and whether or not it is blocked.
 * Also provides access to the hamsters list and the amount of grains on this tile for convenience.
 */
public interface ObservableTile {
    /**
     * Getter for the grainCount property
     * Always greater than or equal to 0
     * The content property contains as much ObservableGrains as the value of this property
     * @return the property (not null)
     */
    ReadOnlyIntegerProperty grainCountProperty();

    /*@
     @ pure;
     @ requires true;
     @ ensures \result >= 0;
     @ ensures \result == getContent().stream().filter(content -> content instanceof Grain).count();
     */
    /**
     * Returns the amount of grain on this tile.
     * This is always greater than or equal to 0.
     * @return the amount of grains
     */
    int getGrainCount();

    /**
     * Getter for the isBlocked property
     * e.g. a tile is blocked if a wall is on it
     * @return the property  (not null)
     */
    ReadOnlyBooleanProperty isBlockedProperty();

    /*@
     @ pure;
     @ requires true;
     @*/
    /**
     * Returns true if this tile is blocked.
     * e.g. a tile is blocked if a wall is on it. A hamster cannot move on a blocked tile.
     * @return true if this tile is blocked
     */
    boolean isBlocked();

    /**
     * Getter for the content property
     * Provides a read-only list of all contents on this tile
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableTileContent> contentProperty();

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null
     @*/
    /**
     * Returns an unmodifiable list with all contents on this tile
     * @return all contents on this tile (not null)
     */
    List<? extends ObservableTileContent> getContent();

    /**
     * Getter for the hamsters property
     * Provides a read-only list of all hamsters on this tile, therefore this
     * is a sublist of content
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends  ObservableTileContent> hamstersProperty();

    /**
     * the location of this tile on the territory
     * @see ObservableTerritory
     * @return the location (not null)
     */
    Location getLocation();
}
