package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;

public class PickGrainCommand extends HamsterCompositeBaseCommand<CompositeCommandSpecification> {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final Hamster hamster) {
        super(hamster, new CompositeCommandSpecification());
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        this.currentTile = this.hamster.getCurrentTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.pickedGrain = this.currentTile.getAnyContentOfType(Grain.class);
        builder.add(
                this.currentTile.getRemoveContentCommand(this.pickedGrain),
                hamster.getAddGrainCommand(this.pickedGrain));
    }

}
