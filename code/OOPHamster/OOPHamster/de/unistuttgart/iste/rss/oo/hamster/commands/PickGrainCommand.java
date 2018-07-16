package de.unistuttgart.iste.rss.oo.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.Territory;
import de.unistuttgart.iste.rss.oo.hamster.Tile;

public class PickGrainCommand extends HamsterCommand {

    private Grain pickedGrain;

    public PickGrainCommand(final HamsterManipulator manipulator, final Territory territory) {
        super(manipulator, territory);
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
