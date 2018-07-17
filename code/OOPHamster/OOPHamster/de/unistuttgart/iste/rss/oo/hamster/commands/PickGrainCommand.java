package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Tile;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterManipulator;

public class PickGrainCommand extends HamsterCommand {

    private Grain pickedGrain;

    public PickGrainCommand(final HamsterManipulator manipulator) {
        super(manipulator);
    }

    @Override
    public void execute() {
        final Tile currentHamsterTile = this.territory.getTileAt(this.hamster.getCurrentPosition());
        this.pickedGrain = currentHamsterTile.getAnyContentOfType(Grain.class);
        currentHamsterTile.removeObjectFromContent(this.pickedGrain);
        this.hamsterManipulator.addGrain(this.pickedGrain);
    }

    @Override
    public void undo() {
    }

}
