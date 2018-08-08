package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class AddGrainsCommand extends CompositeBaseCommand {

    public AddGrainsCommand(final PropertyMap<Tile> tileState, final int amount) {
        super();

        for (int i = 0; i < amount; i++) {
            this.compositeCommandBuilder.addPropertyUpdateCommand(tileState, "content", new Grain(), ActionKind.ADD);
        }
    }
}
