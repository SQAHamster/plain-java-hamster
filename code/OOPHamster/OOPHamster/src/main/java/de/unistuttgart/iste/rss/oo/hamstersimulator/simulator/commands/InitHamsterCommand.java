package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class InitHamsterCommand extends CompositeBaseCommand implements GameCommand {

    protected InitHamsterCommandParameter specification;
    private final Hamster hamster;
    private final Territory territory;

    public InitHamsterCommand(final Hamster hamster, final Territory territory, final InitHamsterCommandParameter specification) {
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
            final Tile tile = this.territory.getTileAt(this.specification.getLocation().get());
            builder.add(hamster.getSetCurrentTileCommand(Optional.of(tile)));
        });
        IntStream.range(0, specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                hamster.getAddGrainCommand(new Grain())));

    }
}
