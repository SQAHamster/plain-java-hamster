package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.AddGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands.RemoveContentsCommand;

public class PickGrainCommand extends HamsterCompositeBaseCommand {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        this.currentTile = this.hamster.getCurrentTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.pickedGrain = this.currentTile.getAnyContentOfType(Grain.class);
        builder.add(
                new RemoveContentsCommand(this.currentTile.getState(), this.pickedGrain),
                new AddGrainCommand(hamsterState, pickedGrain));
    }

}
