package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class PickGrainCommand extends HamsterCommand {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final HamsterStateChanger stateChanger) {
        super(stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentTile().isPresent();

        this.currentTile = this.getTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.pickedGrain = this.currentTile.getAnyContentOfType(Grain.class);
        this.currentTile.removeObjectFromContent(this.pickedGrain);
        this.stateChanger.addGrainToMouth(this.pickedGrain);
    }

    @Override
    public void undo() {
        this.stateChanger.removeGrainFromMouth(this.pickedGrain);
        this.currentTile.addObjectToContent(pickedGrain);
    }

}
