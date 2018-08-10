package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.List;
import java.util.stream.Collectors;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;

public class ClearTileCommand extends AbstractTerritoryCompositeBaseCommand {

    private final ClearTileCommandSpecification specification;

    public ClearTileCommand(final Territory territory, final ClearTileCommandSpecification spec) {
        super(territory);
        this.specification = spec;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(this.specification.getLocation());
        final List<Command> commands = tile.content.stream().
                filter(content -> content.getClass() != Hamster.class).
                map(content -> UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(tile.content, "content", content, ActionKind.REMOVE)).
                collect(Collectors.toList());
        builder.add(commands);
    }
}
