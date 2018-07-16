package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.Territory;
import de.unistuttgart.iste.rss.oo.hamster.Tile;

public class PutGrainCommand extends HamsterCommand {

    private Grain grainDropped;

    public PutGrainCommand(final HamsterManipulator manipulator, final Territory territory) {
        super(manipulator, territory);
    }

    @Override
    public void execute() {
        final Tile currentHamsterTile = this.territory.getTileAt(this.hamster.getCurrentPosition());
        this.grainDropped = this.hamsterManipulator.removeAnyGrain();
        currentHamsterTile.addObjectToContent(this.grainDropped);
    }

    @Override
    public void undo() {
    }

}
