package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommandParameter;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.GameCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.collections.FXCollections;

public class Hamster extends TileContent {

    /*
     * Static part of class, provides a hamster registry
     */
    private static final ReadOnlySetWrapper<Hamster> hamsterSet = new ReadOnlySetWrapper<Hamster>();

    static {
        hamsterSet.set(FXCollections.observableSet());
    }

    public static ReadOnlySetProperty<Hamster> hamsterSetProperty() {
        return hamsterSet.getReadOnlyProperty();

    }

    private final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<>(this, "direction", Direction.NORTH);
    private final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<>(this, "grainInMouth", FXCollections.observableArrayList());
    private final PropertyMap<Hamster> propertyMap = new PropertyMap<>(this, currentTile, direction, grainInMouth);

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster() {
        super();

        hamsterSet.add(this);
    }


    /*
     * Read-Only (observable) Properties
     */
    public ReadOnlyObjectProperty<Direction> directionProperty() {
        return this.direction.getReadOnlyProperty();
    }

    public Direction getDirection() {
        return direction.get();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth.get());
    }

    /*
     * Commands
     */

    public GameCommand getInitializeHamsterCommand(final Optional<Territory> territory, final Optional<Location> location, final Direction newDirection, final int newGrainCount) {
        return new InitHamsterCommand(this, territory, new InitHamsterCommandParameter(location, newDirection, newGrainCount));
    }

    public CommandInterface getRemoveGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.REMOVE);
    }

    public CommandInterface getAddGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.ADD);
    }

    public CommandInterface getSetDirectionCommand(final Direction direction) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "direction", direction, ActionKind.SET);
    }

    public CommandInterface getSetCurrentTileCommand(final Optional<Tile> newTile) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "currentTile", newTile, ActionKind.SET);
    }

    public CommandInterface getCommandFromSpecification(final PropertyCommandSpecification spec) {
        return new UnidirectionalUpdatePropertyCommand<Hamster>(this.propertyMap, spec);
    }

    /*
     * OO-Design Methods
     */
    @Override
    public String toString() {
        return "Hamster";
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}