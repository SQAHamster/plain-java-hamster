package de.unistuttgart.iste.rss.oo.hamster;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamster.datatypes.Location;

public class Tile {

    private final Location tileLocation;
    private final Collection<TileContent> content;

    private Tile(final Location tileLocation, final Collection<? extends TileContent> initialContent) {
        super();
        this.tileLocation = tileLocation;
        this.content = new LinkedList<TileContent>();
        this.content.addAll(initialContent);
    }

    public static Tile createWall(final Location location) {
        return new Tile(location, Arrays.asList(new Wall()));
    }

    public static Tile createGrainTile(final Location location) {
        return new Tile(location, Collections.emptyList());
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
    }

    public void removeObjectFromContent(final TileContent objectToRemove) {
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

}