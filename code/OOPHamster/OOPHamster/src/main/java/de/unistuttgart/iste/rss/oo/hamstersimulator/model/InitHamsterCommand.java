package de.unistuttgart.iste.rss.oo.hamstersimulator.model;

import java.util.Optional;
import java.util.stream.IntStream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;

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
                hamster.getSetCurrentTileCommand(Optional.empty()),
                hamster.getSetDirectionCommand(this.specification.getNewDirection()));
        this.specification.getLocation().ifPresent(location -> {
            final Tile tile = this.territory.getTileAt(this.specification.getLocation().get());
            builder.add(hamster.getSetCurrentTileCommand(Optional.of(tile)));
        });
        IntStream.range(0, this.specification.getNewGrainCount()).forEach(i -> this.compositeCommandBuilder.add(
                hamster.getAddGrainCommand(new Grain())));

    }

}
