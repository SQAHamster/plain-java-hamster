package de.hamstersimulator.objectsfirst.internal.model.territory;

import java.util.Optional;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableTileContent;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class TileContent implements ObservableTileContent {

    protected final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>(this,"currentTile", Optional.empty());

    public /*@ pure @*/ Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     */
    /**
     * Get the current tileContent location.
     * @return The current hamster's location in the territory.
     */
    @Override
    public Optional<Location> getCurrentLocation() {
        return getCurrentTile().map(Tile::getLocation);
    }

    /**
     * Getter for the currentTile property
     * Provides access to the tile on which this content currently is.
     * A content must not always be on a tile, so this is optional
     * @return the property (not null)
     */
    @Override
    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    protected abstract boolean blocksEntrance();

}
