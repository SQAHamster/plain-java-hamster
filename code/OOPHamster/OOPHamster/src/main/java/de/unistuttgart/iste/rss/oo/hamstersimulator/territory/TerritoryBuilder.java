package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Wall;

public class TerritoryBuilder {

    private final Territory territory;
    private final LinkedList<AbstractBaseCommand<?>> commands = new LinkedList<>();

    TerritoryBuilder(final Territory territory) {
        super();
        this.territory = territory;
    }

    public TerritoryBuilder initializeTerritory(final Dimension size) {
        this.commands.add(this.territory.getSetTerritorySizeCommand(size));
        return this;
    }

    public TerritoryBuilder wallAt(final Location location) {
        this.commands.add(this.territory.getAddContentCommand(location, new Wall()));
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final Optional<Location> location, final Direction direction, final int grainCount) {
        final AbstractBaseCommand<InitHamsterCommandSpecification> initCommand = this.territory.getDefaultHamster().getInitializeHamsterCommand(
                this.territory,
                location,
                direction,
                grainCount);
        this.commands.add(initCommand);
        return this;
    }

    public TerritoryBuilder grainAt(final Location location) {
        return this.grainAt(location, 1);
    }

    public TerritoryBuilder grainAt(final Location location, final int grainCount) {
        for (int i = 0; i < grainCount; i++) {
            this.commands.add(this.territory.getAddContentCommand(location, new Grain()));
        }
        return this;
    }

    public void build() {
        this.territory.getCommandStack().execute(
                new CompositeBaseCommand<CompositeCommandSpecification>(new CompositeCommandSpecification()) {
                    @Override
                    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                        builder.add(commands);
                    }
                });
    }

}
