package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.GameCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class InitHamsterCommand extends CompositeBaseCommand implements GameCommand {

    protected InitHamsterCommandParameter specification;
    private final Hamster hamster;
    private final Optional<Territory> territory;

    public InitHamsterCommand(final Hamster hamster, final Optional<Territory> territory, final InitHamsterCommandParameter specification) {
        super();
        this.specification = specification;
        this.hamster = hamster;
        this.territory = territory;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        builder.add(
                hamster.getSetCurrentTileCommand(Optional.empty()),
                hamster.getSetDirectionCommand(specification.getNewDirection()));
        this.specification.getLocation().ifPresent(location -> {
            assert this.territory.isPresent();

            final Tile tile = this.territory.get().getTileAt(this.specification.getLocation().get());
            builder.add(hamster.getSetCurrentTileCommand(Optional.of(tile)));
        });
        IntStream.range(0, specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                hamster.getAddGrainCommand(new Grain())));

    }
}
