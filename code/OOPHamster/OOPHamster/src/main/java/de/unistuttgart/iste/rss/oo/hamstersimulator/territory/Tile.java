package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileContentAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileContentRemovedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.events.TileListener;

public class Tile {

    private final Location tileLocation;
    private final Collection<TileContent> content;
    private final Territory territory;
    private final List<TileListener> tileObservers = new LinkedList<>();

    private Tile(final Territory territory, final Location tileLocation, final Collection<? extends TileContent> initialContent) {
        super();
        this.territory = territory;
        this.tileLocation = tileLocation;
        this.content = new LinkedList<TileContent>();
        this.content.addAll(initialContent);
    }

    static Tile createWall(final Territory territory, final Location location) {
        return new Tile(territory, location, Arrays.asList(new Wall()));
    }

    static Tile createEmptyTile(final Territory territory, final Location location) {
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
        this.content.add(newObject);
        this.notifyContentAdded(new TileContentAddedEvent(this, newObject));
    }

    public void removeObjectFromContent(final TileContent objectToRemove) {
        this.content.remove(objectToRemove);
        this.notifyContentRemoved(new TileContentRemovedEvent(this, objectToRemove));
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

    public Location getTileLocation() {
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

    public void addTileListener(final TileListener listener) {
        this.tileObservers.add(listener);
    }

    public void removeTileListener(final TileListener listener) {
        this.tileObservers.remove(listener);
    }

    private void notifyContentAdded(final TileContentAddedEvent e) {
        for (final TileListener observer : tileObservers) {
            observer.contentItemAdded(e);
        }
    }

    private void notifyContentRemoved(final TileContentRemovedEvent e) {
        for (final TileListener observer : tileObservers) {
            observer.contentItemRemoved(e);
        }
    }

    public void dispose() {
        final Collection<TileContent> content = new LinkedList<>(this.content);
        for (final TileContent item : content) {
            this.removeObjectFromContent(item);
        }
    }
}