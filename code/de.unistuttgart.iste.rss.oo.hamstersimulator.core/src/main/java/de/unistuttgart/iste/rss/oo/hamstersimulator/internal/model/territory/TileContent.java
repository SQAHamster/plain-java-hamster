package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTileContent;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class TileContent implements ObservableTileContent {

    protected final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>(this,"currentTile", Optional.empty());

    public /*@ pure @*/ Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }

    @Override
    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    protected abstract boolean blocksEntrance();

}