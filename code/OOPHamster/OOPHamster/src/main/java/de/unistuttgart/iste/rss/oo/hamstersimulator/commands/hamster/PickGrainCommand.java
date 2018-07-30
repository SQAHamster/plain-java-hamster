package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class PickGrainCommand extends HamsterCommand {

    private Grain pickedGrain;
    private Tile currentTile;

    public PickGrainCommand(final Territory territory, final HamsterStateChanger stateChanger) {
        super(territory, stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentPosition().isPresent();

        this.currentTile = this.territory.getTileAt(this.hamster.getCurrentPosition().get());
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
