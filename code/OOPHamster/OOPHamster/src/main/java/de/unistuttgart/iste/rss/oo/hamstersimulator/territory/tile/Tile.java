package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.collections.FXCollections;

public class Tile {

    private final ReadOnlySetWrapper<TileContent> content = new ReadOnlySetWrapper<TileContent>(this, "content", FXCollections.observableSet());
    private final Territory territory;
    private final Location tileLocation;

    private Tile(final Territory territory, final Location tileLocation, final Collection<? extends TileContent> initialContent) {
        super();
        this.territory = territory;
        this.tileLocation = tileLocation;
        this.content.addAll(initialContent);
    }

    static Tile createWall(final Territory territory, final Location location) {
        return new Tile(territory, location, Arrays.asList(new Wall()));
    }

    public static Tile createEmptyTile(final Territory territory, final Location location) {
        return new Tile(territory, location, Collections.emptyList());
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

    public void addObjectToContent(final TileContent newObject) {
        if (this.hasObjectInContent(newObject)) {
            throw new IllegalArgumentException("Object to not be already in the contents.");
        }
        this.content.add(newObject);
    }

    public void removeObjectFromContent(final TileContent objectToRemove) {
        if (!this.hasObjectInContent(objectToRemove)) {
            throw new IllegalArgumentException("Object to be removed not found in tile contents");
        }
        this.content.remove(objectToRemove);
    }

    public boolean hasObjectInContent(final TileContent content) {
        return this.content.contains(content);
    }

    @Override
    public String toString() {
        return "Tile [tileLocation=" + tileLocation + ", content=" + content + "]";
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
            this.removeObjectFromContent(item);
        }
    }
}