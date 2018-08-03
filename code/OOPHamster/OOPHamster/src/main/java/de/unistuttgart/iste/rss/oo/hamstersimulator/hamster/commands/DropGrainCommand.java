package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;

public class DropGrainCommand extends HamsterCommand {

    private final Grain grain;

    public DropGrainCommand(final PropertyMap<Hamster> hamsterState, final Grain grainToAdd) {
        super(hamsterState);
        this.grain = grainToAdd;
    }

    @Override
    public void execute() {
        this.entityState.getListProperty("grainInMouth").remove(this.grain);
    }

    @Override
    public void undo() {
        this.entityState.getListProperty("grainInMouth").add(this.grain);
    }

}
