package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class PickGrainCommand extends HamsterCommand {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentTile().isPresent();

        this.currentTile = this.getTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.pickedGrain = this.currentTile.getAnyContentOfType(Grain.class);
        this.currentTile.removeObjectFromContent(this.pickedGrain);
        this.entityState.getListProperty("grainInMouth").add(this.pickedGrain);
    }

    @Override
    public void undo() {
        this.entityState.getListProperty("grainInMouth").remove(this.pickedGrain);
        this.currentTile.addObjectToContent(pickedGrain);
    }

}
