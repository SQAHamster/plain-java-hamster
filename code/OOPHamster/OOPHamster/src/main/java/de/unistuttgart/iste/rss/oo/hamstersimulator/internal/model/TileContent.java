package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class TileContent {

    protected final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>(this,"currentTile", Optional.empty());

    public TileContent() {
        super();
        this.currentTile.addListener((property, oldValue, newValue) -> {
            oldValue.ifPresent(tile -> tile.getState().getSetProperty("content").remove(this));
            newValue.ifPresent(tile -> tile.getState().getSetProperty("content").add(this));
        });
    }

    public Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }


    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    protected abstract boolean blocksEntrance();

    void setCurrentTile(final Optional<Tile> tile) {
        this.currentTile.setValue(tile);
    }

}