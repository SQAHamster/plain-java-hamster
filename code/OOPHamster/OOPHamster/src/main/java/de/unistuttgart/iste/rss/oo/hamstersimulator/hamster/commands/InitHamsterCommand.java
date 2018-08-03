package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands.SetCurrentTileCommand;

public class InitHamsterCommand extends CompositeBaseCommand {

    public InitHamsterCommand(final PropertyMap<Hamster> hamsterState, final Optional<Tile> newTile, final Direction newDirection, final int newGrainCount) {
        super();
        this.compositeCommandBuilder.add(
                new SetCurrentTileCommand<Hamster>(hamsterState, Optional.empty()),
                new SetDirectionCommand(hamsterState, newDirection),
                new SetCurrentTileCommand<Hamster>(hamsterState, newTile));
        IntStream.of(1,newGrainCount).forEach(i -> this.compositeCommandBuilder.add(new AddGrainCommand(hamsterState, new Grain())));
    }

}
