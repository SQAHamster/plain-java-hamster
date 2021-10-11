package de.hamstersimulator.objectsfirst.internal.model.territory;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTerritory;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.internal.model.hamster.ReadOnlyHamster;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

public class Tile implements ObservableTile {

    final ReadOnlyListWrapper<TileContent> content = new ReadOnlyListWrapper<TileContent>(this, "content", FXCollections.observableArrayList());
    final ReadOnlyIntegerWrapper grainCount = new ReadOnlyIntegerWrapper(this, "grainCount", 0);
    final ReadOnlyBooleanWrapper isBlocked = new ReadOnlyBooleanWrapper(this, "isBlocked", false);
    private final ReadOnlyListWrapper<TileContent> grainSublist;
    private final ReadOnlyListWrapper<TileContent> blockerSublist;
    private final ReadOnlyListWrapper<TileContent> hamsterSublist;

    private final ReadOnlyTerritory territory;
    private final Location tileLocation;

    Tile(final ReadOnlyTerritory territory, final Location tileLocation) {
        super();

        checkNotNull(territory);
        checkNotNull(tileLocation);

        this.territory = territory;
        this.tileLocation = tileLocation;

        this.grainSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c instanceof Grain));
        this.blockerSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c.blocksEntrance()));
        this.hamsterSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c instanceof ReadOnlyHamster));

        this.grainCount.bind(this.grainSublist.sizeProperty());
        this.isBlocked.bind(Bindings.createBooleanBinding(() -> this.blockerSublist.size() > 0, this.blockerSublist.sizeProperty()));
    }

    public ReadOnlyTerritory getTerritory() {
        return territory;
    }

    /**
     * the location of this tile on the territory
     * @see ObservableTerritory
     * @return the location (not null)
     */
    @Override
    public /*@ pure @*/ Location getLocation() {
        return tileLocation;
    }

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
    @Override
    public int getGrainCount() {
        return this.grainCount.get();
    }

    /*@
     @ pure;
     @ requires true;
     @*/
    /**
     * Returns true if this tile is blocked.
     * e.g. a tile is blocked if a wall is on it. A hamster cannot move on a blocked tile.
     * @return true if this tile is blocked
     */
    @Override
    public boolean isBlocked() {
        return isBlocked.get();
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null
     @*/
    /**
     * Returns an unmodifiable list with all contents on this tile
     * @return all contents on this tile (not null)
     */
    @Override
    public List<TileContent> getContent() {
        return Collections.unmodifiableList(this.content.get());
    }

    public List<? extends TileContent> getHamsters() {
        return Collections.unmodifiableList(this.hamsterSublist.get());
    }

    /**
     * Getter for the grainCount property
     * Always greater than or equal to 0
     * the content property contains as much ObservableGrains as the value of this property
     * @return the property (not null)
     */
    @Override
    public ReadOnlyIntegerProperty grainCountProperty() {
        return this.grainCount.getReadOnlyProperty();
    }

    /**
     * Getter for the isBlocked property
     * e.g. a tile is blocked if a wall is on it
     * @return the property  (not null)
     */
    @Override
    public ReadOnlyBooleanProperty isBlockedProperty() {
        return this.isBlocked.getReadOnlyProperty();
    }

    /**
     * Getter for the content property
     * Provides a read-only list of all contents on this tile
     * @return the property (not null)
     */
    @Override
    public ReadOnlyListProperty<TileContent> contentProperty() {
        return this.content.getReadOnlyProperty();
    }

    /**
     * Getter for the hamsters property
     * Provides a read-only list of all hamsters on this tile, therefore this
     * is a sublist of content
     * @return the property (not null)
     */
    @Override
    public ReadOnlyListProperty<TileContent> hamstersProperty() {
        return this.hamsterSublist.getReadOnlyProperty();
    }

    public void dispose() {
        final Collection<TileContent> content = new LinkedList<>(this.content);
        for (final TileContent item : content) {
            this.content.remove(item);
        }
    }

    @Override
    public String toString() {
        return "Tile [tileLocation=" + tileLocation + ", content=" + content + "]";
    }

}
