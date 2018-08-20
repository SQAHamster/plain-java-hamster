package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecifiedCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;

public class InitHamsterCommand extends AbstractHamsterCompositeBaseCommand implements SpecifiedCommand<InitHamsterCommandSpecification> {

    private final ReadOnlyTerritory territory;
    private final InitHamsterCommandSpecification specification;

    public InitHamsterCommand(final ReadOnlyTerritory territory, final InitHamsterCommandSpecification specification) {
        this(territory.getDefaultHamster(), territory, specification);
    }

    public InitHamsterCommand(final Hamster hamster, final ReadOnlyTerritory territory, final InitHamsterCommandSpecification specification) {
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
            builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(tile.content, this.hamster, ActionKind.ADD));
        });
        IntStream.range(0, this.specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.grainInMouth, new Grain(), ActionKind.ADD)));
    }

    @Override
    public InitHamsterCommandSpecification getSpecification() {
        return this.specification;
    }

}
