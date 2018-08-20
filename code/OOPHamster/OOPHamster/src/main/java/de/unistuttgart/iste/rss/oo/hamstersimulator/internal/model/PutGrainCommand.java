package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;

public class PutGrainCommand extends AbstractHamsterCompositeBaseCommand {

    private Grain grainDropped;

    public PutGrainCommand(final Hamster hamster) {
        super(hamster);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        final Tile currentTile = this.hamster.getCurrentTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.grainDropped = this.hamster.getGrainInMouth().get(0);
        builder.add(
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.grainInMouth, this.grainDropped, ActionKind.REMOVE),
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(currentTile.content, this.grainDropped, ActionKind.ADD),
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.grainDropped.currentTile, Optional.of(currentTile), ActionKind.SET));
    }
}
