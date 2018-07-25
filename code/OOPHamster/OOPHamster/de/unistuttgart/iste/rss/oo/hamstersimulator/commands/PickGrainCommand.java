package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

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
