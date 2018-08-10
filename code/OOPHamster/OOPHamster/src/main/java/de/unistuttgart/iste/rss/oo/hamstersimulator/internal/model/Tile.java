package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;

public class Tile {

    final ReadOnlySetWrapper<TileContent> content = new ReadOnlySetWrapper<TileContent>(this, "content", FXCollections.observableSet());

    private final Territory territory;
    private final Location tileLocation;

    Tile(final Territory territory, final Location tileLocation) {
        super();

        checkNotNull(territory);
        checkNotNull(tileLocation);

        this.territory = territory;
        this.tileLocation = tileLocation;

        this.content.addListener(new SetChangeListener<TileContent>() {

            @Override
            public void onChanged(final Change<? extends TileContent> change) {
                if (change.wasAdded()) {
                    if (!change.getElementAdded().getCurrentTile().isPresent()) {
                        change.getElementAdded().setCurrentTile(Optional.of(Tile.this));
                    } else {
                        change.getElementAdded().getCurrentTile().ifPresent(tile -> {
                            if (tile != Tile.this) {
                                change.getElementAdded().setCurrentTile(Optional.of(Tile.this));
                            }
                        });
                    }
                }
                if (change.wasRemoved()) {
                    if (change.getElementRemoved().getCurrentTile().isPresent()) {
                        change.getElementRemoved().setCurrentTile(Optional.empty());
                    }
                }
            }
        });
    }

    public Territory getTerritory() {
        return territory;
    }

    public boolean canEnter() {
        for (final TileContent c : content) {
            if (c.blocksEntrance()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasObjectInContent(final TileContent content) {
        return this.content.contains(content);
    }

    public int countObjectsOfType(final Class<?> clazz) {
        int count = 0;
        for (final TileContent c : content) {
            if (clazz.isInstance(c)) {
                count++;
            }
        }
        return count;
    }

    public ReadOnlySetProperty<TileContent> contentProperty() {
        return this.content.getReadOnlyProperty();
    }

    public Location getLocation() {
        return tileLocation;
    }

    @SuppressWarnings("unchecked")
    public <R extends TileContent> R getAnyContentOfType(final Class<?> clazz) {
        for (final TileContent c : content) {
            if (clazz.isInstance(c)) {
                return (R)c;
            }
        }
        throw new RuntimeException();
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