package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class PutGrainCommand extends HamsterCommand {

    private Grain grainDropped;
    private Tile currentTile;

    public PutGrainCommand(final HamsterStateChanger stateChanger) {
        super(stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentTile().isPresent();
        assert !this.hamster.getGrainInMouth().isEmpty();

        this.currentTile = this.getTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());
        this.grainDropped = this.stateChanger.getAnyGrain();
        this.currentTile.addObjectToContent(this.grainDropped);
        this.stateChanger.removeGrainFromMouth(this.grainDropped);
    }

    @Override
    public void undo() {
        this.currentTile.removeObjectFromContent(this.grainDropped);
        this.stateChanger.addGrainToMouth(this.grainDropped);
    }

}
