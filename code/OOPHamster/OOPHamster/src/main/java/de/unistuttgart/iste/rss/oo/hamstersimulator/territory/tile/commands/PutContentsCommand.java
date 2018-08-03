package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

public class PutContentsCommand extends TileCommand {

    private final Collection<TileContent> contentToPut = new LinkedList<>();

    public PutContentsCommand(final PropertyMap<Tile> tileState, final TileContent ... contents) {
        this(tileState, Arrays.asList(contents));
    }

    public PutContentsCommand(final PropertyMap<Tile> tileState, final Collection<? extends TileContent> newContent) {
        super(tileState);
        contentToPut.addAll(newContent);
    }

    @Override
    public void execute() {
        this.contentToPut.forEach(newContent -> {
            checkArgument(!this.entityState.getSetProperty("content").contains(newContent), "Object to not be already in the contents.");
            this.entityState.<TileContent> getSetProperty("content").add(newContent);
        });
    }

    @Override
    public void undo() {
        this.contentToPut.forEach(newContent -> this.entityState.<TileContent> getSetProperty("content").remove(newContent));
    }

}
