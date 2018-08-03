package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.PutContentsCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Wall;

public class TerritoryBuilder {

    private final Territory territory;
    private final HamsterSimulator simulator;

    public TerritoryBuilder(final HamsterSimulator simulator, final Territory territory) {
        super();
        this.territory = territory;
        this.simulator = simulator;
    }

    public TerritoryBuilder wallAt(final int row, final int column) {
        this.simulator.getCommandStack()
        .execute(new PutContentsCommand(this.territory.getTileAt(new Location(row, column)), new Wall()));
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final int row, final int column, final Direction direction, final int grainCount) {
        this.territory.getDefaultHamster().init(
                Optional.of(this.territory.getTileAt(new Location(row, column))),
                direction,
                grainCount);
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
