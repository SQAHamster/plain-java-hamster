package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

public class RemoveContentsCommand extends TileCommand {

    private final List<TileContent> contentToRemove = new LinkedList<>();

    public RemoveContentsCommand(final PropertyMap<Tile> tileState, final TileContent ... contents) {
        this(tileState, Arrays.asList(contents));
    }

    public RemoveContentsCommand(final PropertyMap<Tile> tileState, final Collection<TileContent> toRemove) {
        super(tileState);
        contentToRemove.addAll(toRemove);
    }

    @Override
    public void execute() {
        this.contentToRemove.forEach(newContent -> {
            checkArgument(this.entityState.<TileContent> getSetProperty("content").contains(newContent), "Object to be removed not found in tile contents");
            this.entityState.<TileContent> getSetProperty("content").remove(newContent);
        });
    }

    @Override
    public void undo() {
        this.contentToRemove.forEach(newContent -> this.entityState.<TileContent> getSetProperty("content").add(newContent));
    }

}
