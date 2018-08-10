package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.InitHamsterCommandSpecification;

public class InitHamsterCommand extends AbstractHamsterCompositeBaseCommand {

    private final Territory territory;
    private final InitHamsterCommandSpecification specification;

    public InitHamsterCommand(final Hamster hamster, final Territory territory, final InitHamsterCommandSpecification specification) {
        super(hamster);
        this.territory = territory;
        this.specification = specification;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        builder.add(
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.currentTile, Optional.empty(), ActionKind.SET),
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.direction, this.specification.getNewDirection(), ActionKind.SET));
        this.specification.getLocation().ifPresent(location -> {
            final Tile tile = this.territory.getTileAt(this.specification.getLocation().get());
            builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.currentTile, Optional.of(tile), ActionKind.SET));
        });
        IntStream.range(0, this.specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.grainInMouth, new Grain(), ActionKind.ADD)));

    }

}
