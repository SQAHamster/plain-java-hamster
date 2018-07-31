package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.ArrayList;
import java.util.Collection;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.territory.PutContentsCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.territory.RemoveContentsCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class TerritoryBuilder {

    private final Territory territory;
    private final HamsterSimulator simulator;

    public TerritoryBuilder(final HamsterSimulator simulator, final Territory territory) {
        super();
        this.territory = territory;
        this.simulator = new HamsterSimulator();
    }

    public TerritoryBuilder wallAt(final int row, final int column) {
        this.simulator.getCommandStack()
        .execute(new PutContentsCommand(this.territory.getTileAt(new Location(row, column)), new Wall()));
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final int row, final int column, final Direction direction,
            final int grainCount) {
        this.territory.getDefaultHamster().getCurrentPosition().ifPresent(pos -> {
            this.simulator.getCommandStack().execute(new RemoveContentsCommand(
                    this.territory.getTileAt(new Location(row, column)), this.territory.getDefaultHamster()));
        });
        this.territory.getDefaultHamster().init(
                null,
                direction,
                grainCount);
        this.simulator.getCommandStack().execute(new PutContentsCommand(
                this.territory.getTileAt(new Location(row, column)), this.territory.getDefaultHamster()));
        return this;
    }

    public TerritoryBuilder grainAt(final int row, final int column) {
        return this.grainAt(row, column, 1);
    }

    public TerritoryBuilder grainAt(final int row, final int column, final int grainCount) {
        this.simulator.getCommandStack().execute(new PutContentsCommand(
                this.territory.getTileAt(new Location(row, column)), createNewGrain(grainCount)));
        return this;
    }

    private Collection<TileContent> createNewGrain(final int count) {
        final Collection<TileContent> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Grain());
        }
        return result;
    }
}
