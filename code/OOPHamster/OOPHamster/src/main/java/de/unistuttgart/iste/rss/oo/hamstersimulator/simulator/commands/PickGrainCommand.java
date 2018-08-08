package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class PickGrainCommand extends HamsterCompositeBaseCommand {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final GameHamster hamster) {
        super(hamster);
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
