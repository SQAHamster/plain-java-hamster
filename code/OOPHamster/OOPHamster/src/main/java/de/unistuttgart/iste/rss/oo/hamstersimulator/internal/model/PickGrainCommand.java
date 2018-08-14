package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;

public class PickGrainCommand extends AbstractHamsterCompositeBaseCommand {

    private Grain pickedGrain;

    public PickGrainCommand(final Hamster hamster) {
        super(hamster);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        final Tile currentTile = this.hamster.getCurrentTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.pickedGrain = (Grain) currentTile.getContent().stream().filter(content -> content instanceof Grain).findFirst().get();
        builder.add(
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(currentTile.content, this.pickedGrain, ActionKind.REMOVE),
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.grainInMouth, this.pickedGrain, ActionKind.ADD),
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.pickedGrain.currentTile, Optional.empty(), ActionKind.SET));
    }

}
