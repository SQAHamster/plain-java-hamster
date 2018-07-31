package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.territory;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;

public class PutContentsCommand extends TileCommand {

    private final Collection<TileContent> contentToPut = new LinkedList<>();

    public PutContentsCommand(final Tile tile, final TileContent ... contents) {
        this(tile, Arrays.asList(contents));
    }

    public PutContentsCommand(final Tile tile, final Collection<? extends TileContent> newContent) {
        super(tile);
        contentToPut.addAll(newContent);
    }

    @Override
    public void execute() {
        for (final TileContent newContent : contentToPut) {
            this.getTile().addObjectToContent(newContent);
        }
    }

    @Override
    public void undo() {
        for (final TileContent newContent : contentToPut) {
            this.getTile().removeObjectFromContent(newContent);
        }
    }

}
