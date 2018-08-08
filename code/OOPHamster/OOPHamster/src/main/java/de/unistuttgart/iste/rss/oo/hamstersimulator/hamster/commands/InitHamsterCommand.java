package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;

public class InitHamsterCommand extends CompositeBaseCommand {

    protected InitHamsterCommandParameter specification;

    public InitHamsterCommand(final Hamster hamster, final InitHamsterCommandParameter specification) {
        super();
        this.specification = specification;
        this.compositeCommandBuilder.add(
                hamster.getSetCurrentTileCommand(Optional.empty()),
                hamster.getSetDirectionCommand(specification.getNewDirection()),
                hamster.getSetCurrentTileCommand(specification.getNewTile()));
        IntStream.range(0, specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                hamster.getAddGrainCommand(new Grain())));
    }

}
