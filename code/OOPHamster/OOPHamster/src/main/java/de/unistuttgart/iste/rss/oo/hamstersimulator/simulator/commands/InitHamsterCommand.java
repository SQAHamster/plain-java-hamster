package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;

public class InitHamsterCommand extends HamsterCompositeBaseCommand<InitHamsterCommandSpecification> {

    private final Territory territory;

    public InitHamsterCommand(final Hamster hamster, final Territory territory, final InitHamsterCommandSpecification specification) {
        super(hamster, specification);
        this.territory = territory;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        builder.add(
                hamster.getSetCurrentTileCommand(Optional.empty()),
                hamster.getSetDirectionCommand(getSpecification().getNewDirection()));
        getSpecification().getLocation().ifPresent(location -> {
            final Tile tile = this.territory.getTileAt(getSpecification().getLocation().get());
            builder.add(hamster.getSetCurrentTileCommand(Optional.of(tile)));
        });
        IntStream.range(0, getSpecification().getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                hamster.getAddGrainCommand(new Grain())));

    }

}
