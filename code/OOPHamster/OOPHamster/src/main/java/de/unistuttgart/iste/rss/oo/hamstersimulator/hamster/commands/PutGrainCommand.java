package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class PutGrainCommand extends HamsterCommand {

    private Grain grainDropped;
    private Tile currentTile;

    public PutGrainCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentTile().isPresent();
        assert !this.hamster.getGrainInMouth().isEmpty();

        this.currentTile = this.getTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.grainDropped = this.entityState.<Grain> getListProperty("grainInMouth").get(0);
        this.currentTile.addObjectToContent(this.grainDropped);
        this.entityState.getListProperty("grainInMouth").remove(this.grainDropped);
    }

    @Override
    public void undo() {
        this.currentTile.removeObjectFromContent(this.grainDropped);
        this.entityState.getListProperty("grainInMouth").add(this.grainDropped);
    }

}
