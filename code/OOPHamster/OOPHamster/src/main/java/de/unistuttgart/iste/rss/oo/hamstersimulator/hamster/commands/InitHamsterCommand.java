package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class InitHamsterCommand extends CompositeBaseCommand {

    public InitHamsterCommand(final PropertyMap<Hamster> hamsterState, final Optional<Tile> newTile, final Direction newDirection, final int newGrainCount) {
        super();
        this.compositeCommandBuilder.add(
                new UnidirectionalUpdatePropertyCommand<Hamster>(hamsterState, new PropertyCommandSpecification("currentTile", Optional.empty(), ActionKind.SET)),
                new SetDirectionCommand(hamsterState, newDirection),
                new UnidirectionalUpdatePropertyCommand<Hamster>(hamsterState, new PropertyCommandSpecification("currentTile", newTile, ActionKind.SET)));
        IntStream.range(1, newGrainCount).forEach(i -> this.compositeCommandBuilder.add(new AddGrainCommand(hamsterState, new Grain())));
    }

}
