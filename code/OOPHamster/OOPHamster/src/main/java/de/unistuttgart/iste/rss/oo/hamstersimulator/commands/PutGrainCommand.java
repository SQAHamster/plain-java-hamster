package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class PutGrainCommand extends HamsterCommand {

    private Grain grainDropped;
    private Tile currentTile;

    public PutGrainCommand(final Territory territory, final HamsterStateChanger stateChanger) {
        super(territory, stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentPosition().isPresent();
        assert this.hamster.grainAvailable();

        this.currentTile = this.territory.getTileAt(this.hamster.getCurrentPosition().get());
        this.grainDropped = this.stateChanger.getAnyGrain();
        this.stateChanger.removeGrainFromMouth(this.grainDropped);
        this.currentTile.addObjectToContent(this.grainDropped);
    }

    @Override
    public void undo() {
        this.currentTile.removeObjectFromContent(this.grainDropped);
        this.stateChanger.addGrainToMouth(this.grainDropped);
    }

}
