package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;

public class InitHamsterCommand extends CompositeBaseCommand {

    protected InitHamsterCommandParameter specification;

    public InitHamsterCommand(final PropertyMap<Hamster> hamsterState, final InitHamsterCommandParameter specification) {
        super();
        this.specification = specification;
        this.compositeCommandBuilder.add(
                createPropertyUpdateCommand(hamsterState, "currentTile", Optional.empty(), ActionKind.SET),
                createPropertyUpdateCommand(hamsterState, "direction", specification.getNewDirection(), ActionKind.SET),
                createPropertyUpdateCommand(hamsterState, "currentTile", specification.getNewTile(), ActionKind.SET));
        IntStream.range(0, specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                createPropertyUpdateCommand(hamsterState, "grainInMouth", new Grain(), ActionKind.ADD)));
    }

}
