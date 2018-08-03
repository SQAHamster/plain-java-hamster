package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.collections.FXCollections;

public class Tile {

    private final ReadOnlySetWrapper<TileContent> content = new ReadOnlySetWrapper<TileContent>(this, "content", FXCollections.observableSet());
    private final PropertyMap<Tile> tileState = new PropertyMap<Tile>(this, content);

    private final Territory territory;
    private final Location tileLocation;

    private Tile(final Territory territory, final Location tileLocation) {
        super();
        checkNotNull(territory);
        checkNotNull(tileLocation);
        checkArgument(territory.isLocationInTerritory(tileLocation));
        this.territory = territory;
        this.tileLocation = tileLocation;
    }

    public static Tile createEmptyTile(final Territory territory, final Location location) {
        return new Tile(territory, location);
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

    public PropertyMap<Tile> getState() {
        return this.tileState;
    }
}