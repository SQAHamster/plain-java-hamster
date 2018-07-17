package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Tile;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterManipulator;

public class PutGrainCommand extends HamsterCommand {

    private Grain grainDropped;

    public PutGrainCommand(final HamsterManipulator manipulator) {
        super(manipulator);
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
