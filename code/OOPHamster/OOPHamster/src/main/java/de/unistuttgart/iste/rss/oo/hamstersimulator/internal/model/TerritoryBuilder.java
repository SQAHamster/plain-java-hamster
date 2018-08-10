package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.model.InitHamsterCommandSpecification;

public class TerritoryBuilder {

    private final Territory territory;
    private final LinkedList<Command> commands = new LinkedList<>();

    TerritoryBuilder(final Territory territory) {
        super();
        this.territory = territory;
    }

    public TerritoryBuilder initializeTerritory(final Dimension size) {
        this.commands.add(new InitializeTerritoryCommand(this.territory, new InitializeTerritoryCommandSpecification(size)));
        return this;
    }

    public TerritoryBuilder wallAt(final Location location) {
        this.commands.add(new AddWallToTileCommand(this.territory, new AddWallToTileCommandSpecification(location)));
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final Optional<Location> location, final Direction direction, final int grainCount) {
        this.commands.add(new InitHamsterCommand(this.territory.getDefaultHamster(), this.territory, new InitHamsterCommandSpecification(location, direction, grainCount)));
        return this;
    }

    public TerritoryBuilder grainAt(final Location location, final int grainCount) {
        this.commands.add(new AddGrainsToTileCommand(this.territory, new AddGrainsToTileCommandSpecification(location, grainCount)));
        return this;
    }

    public TerritoryBuilder grainAt(final Location location) {
        return this.grainAt(location, 1);
    }

    public void build() {
        this.territory.getCommandStack().execute(
                new AbstractCompositeCommand() {
                    @Override
                    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                        builder.add(commands);
                    }
                });
    }

}
