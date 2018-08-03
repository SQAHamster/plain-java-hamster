package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

public class RemoveContentsCommand extends TileCommand {

    private final Collection<TileContent> contentToRemove = new LinkedList<>();

    public RemoveContentsCommand(final Tile tile, final TileContent ... contents) {
        this(tile, Arrays.asList(contents));
    }

    public RemoveContentsCommand(final Tile tile, final Collection<? extends TileContent> newContent) {
        super(tile);
        contentToRemove.addAll(newContent);
    }

    @Override
    public void execute() {
        for (final TileContent newContent : contentToRemove) {
            this.getTile().removeObjectFromContent(newContent);
        }
    }

    @Override
    public void undo() {
        for (final TileContent newContent : contentToRemove) {
            this.getTile().addObjectToContent(newContent);
        }
    }

}
