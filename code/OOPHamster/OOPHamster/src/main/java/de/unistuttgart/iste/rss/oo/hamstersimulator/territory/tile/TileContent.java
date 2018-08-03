package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile;

import java.util.Optional;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class TileContent {

    protected final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>(this,"currentTile", Optional.empty());

    public Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }


    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    protected abstract boolean blocksEntrance();

}