package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class PutGrainCommand extends HamsterCompositeBaseCommand<CompositeCommandSpecification> {

    private Grain grainDropped;
    private Tile currentTile;

    public PutGrainCommand(final Hamster hamster) {
        super(hamster, new CompositeCommandSpecification());
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        this.currentTile = this.hamster.getCurrentTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.grainDropped = this.hamster.getGrainInMouth().get(0);
        builder.add(
                this.hamster.getRemoveGrainCommand(this.grainDropped),
                this.currentTile.getAddContentCommand(this.grainDropped));
    }
}
