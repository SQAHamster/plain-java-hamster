package de.hamstersimulator.objectsfirst.external.model;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkArgument;
import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;
import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkState;

import java.util.Collection;
import java.util.Optional;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.LocationVector;
import de.hamstersimulator.objectsfirst.exceptions.FrontBlockedException;
import de.hamstersimulator.objectsfirst.exceptions.MouthEmptyException;
import de.hamstersimulator.objectsfirst.exceptions.NoGrainOnTileException;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.WriteCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.Grain;
import de.hamstersimulator.objectsfirst.internal.model.territory.Tile;
import de.hamstersimulator.objectsfirst.internal.model.territory.TileContent;

/**
 * Class for Hamster in the Hamster Simulator. This class allows to initialize
 * Hamsters, command Hamsters through their territory and query their state.
 *
 * @author Steffen Becker
 *
 */
public class Hamster {

    /**
     * The game this hamster takes part in.
     */
    private HamsterGame game;

    /*@
     @ public model instance boolean isInitialized;
     @ private represents isInitialized = (game != null);
     @ public model instance int grainCount;
     @ private represents grainCount <- internalHamster.getGrainCount();
     @*/
    /**
     * The internal hamster object representing this hamster in the
     * simulator internal model.
     */
    private final GameHamster internalHamster;

    /**
     * Create a new Hamster object without initializing it.
     */
    public Hamster() {
        super();
        this.internalHamster = new GameHamster();
    }


    /*@
    @ requires territory != null;
    @ requires location != null;
    @ requires territory.isLocationInTerritory(location);
    @ requires ! territory.isBlockedByWall(location);
    @ requires newGrainCount >= 0;
    @ requires direction != null;
    @ ensures isInitialized;
    @ ensures grainCount == newGrainCount;
    @*/
    /**
     * Create and initialize a new hamster object with the given parameters.
     * @param territory The territory this hamster lives in.
     * @param location The location in the territory, where the hamster starts.
     * @param direction The direction in which the hamster looks initially.
     * @param newGrainCount The number of grain objects initially placed into the
     *        hamster's mouth.
     */
    public Hamster(final Territory territory, final Location location,
                   final Direction direction, final int newGrainCount) {
        this();
        init(territory, location, direction, newGrainCount);
    }

    /**
     * Internal constructor, used for the default hamster of a territory.
     * @param territory The territory for which the default hamster should be
     *                  initialized.
     */
    private Hamster(final Territory territory) {
        super();
        this.game = territory.getGame();
        this.internalHamster = territory.getInternalTerritory().getDefaultHamster();
        territory.registerHamster(this, this.internalHamster);
    }

    /*
     * Commands
     */
    /*@
     @ requires territory != null;
     @ requires location != null;
     @ requires territory.isLocationInTerritory(location);
     @ requires ! territory.isBlockedByWall(location);
     @ requires newGrainCount >= 0;
     @ requires direction != null;
     @ ensures isInitialized;
     @ ensures grainCount == newGrainCount;
     @*/
    /**
     * Initialize a hamster object with the given parameters. Once a hamster
     * is initialized, the method must not be called again.
     * @param territory The territory this hamster lives in.
     * @param location The location in the territory, where the hamster starts.
     * @param direction The direction in which the hamster looks initially.
     * @param newGrainCount The number of grain objects initially placed into
     *                   the hamster's mouth
     */
    public void init(final Territory territory, final Location location,
                     final Direction direction, final int newGrainCount) {
        checkNotNull(territory);
        checkNotNull(location);
        checkNotNull(direction);
        checkArgument(newGrainCount >= 0);
        checkState(this.game == null, "Hamster is already initialized!");
        this.game = territory.getGame();
        this.game.processCommandSpecification(
                new InitHamsterCommandSpecification(
                        this.internalHamster,
                        territory.getInternalTerritory(),
                        location,
                        direction,
                        newGrainCount));
        territory.registerHamster(this, this.internalHamster);
    }


    /*@
     @ requires frontIsClear();
     @ requires isInitialized;
     @ ensures getDirection() == Direction.NORTH ==>
     @      \old(getLocation()).getRow() == getLocation().getRow() + 1 &&
     @      \old(getLocation()).getColumn() == getLocation().getColumn();
     @ ensures getDirection() == Direction.SOUTH ==>
     @      \old(getLocation()).getRow() == getLocation().getRow() - 1 &&
     @      \old(getLocation()).getColumn() == getLocation().getColumn();
     @ ensures getDirection() == Direction.EAST ==>
     @      \old(getLocation()).getRow() == getLocation().getRow() &&
     @      \old(getLocation()).getColumn() == getLocation().getColumn() - 1;
     @ ensures getDirection() == Direction.WEST ==>
     @      \old(getLocation()).getRow() == getLocation().getRow() &&
     @      \old(getLocation()).getColumn() == getLocation().getColumn() + 1;
     @*/
    /**
     * Move the hamster one step towards its looking direction.
     * @throws FrontBlockedException When the tile in front of the hamster is blocked
     */
    public void move() {
        this.game.processCommandSpecification(
                new MoveCommandSpecification(this.internalHamster));
    }

    /**
     * Changes the looking direction 90 degrees to the left, e.g.,
     * when looking north the hamster will look towards west afterwards.
     */
    public void turnLeft() {
        this.game.processCommandSpecification(
                new TurnLeftCommandSpecification(this.internalHamster));
    }

    /*@
     @ requires grainAvailable();
     @ requires isInitialized;
     @ ensures grainCount == \old(grainCount) + 1;
     @*/
    /**
     * Pick up a random grain from the tile on which the hamster is
     * currently.
     * @throws NoGrainOnTileException when no more grains are available on
     *         the hamster's tile.
     */
    public void pickGrain() {
        final Tile currentTile = this.internalHamster.getCurrentTile().get();
        final Grain grain = getRandomGrainFromTile(currentTile);
        if (grain == null) {
            throw new NoGrainOnTileException();
        }
        this.game.processCommandSpecification(
                new PickGrainCommandSpecification(this.internalHamster, grain));
    }

    /*@
    @ requires ! mouthEmpty();
    @ requires isInitialized;
    @ ensures grainCount == \old(grainCount) - 1;
    @*/
    /**
     * Drop a random grain object from the hamster's mouth.
     * @throws MouthEmptyException when the hamster does not carry any grain.
     */
    public void putGrain() {
        if (this.internalHamster.getGrainInMouth().isEmpty()) {
            throw new MouthEmptyException();
        }
        this.game.processCommandSpecification(
                new PutGrainCommandSpecification(
                        this.internalHamster,
                        this.internalHamster.getGrainInMouth().get(0)));
    }

    /**
     * Read a number from the hamster simulator UI for further use.
     * @param message The message used to prompt for the number.
     * @return Number read from the user.
     */
    public int readNumber(final String message) {
        return this.game.readInteger(message);
    }

    /**
     * Read a string from the hamster simulator UI for further use.
     * @param message The message used to prompt for the string.
     * @return String read from the user.
     */
    public String readString(final String message) {
        return this.game.readString(message);
    }

    /**
     * Writes a new string for this hamster to the game log. The message
     * will be displayed in a way that it can be associated to this hamster
     * object.
     * @param text The string to write to the log.
     */
    public void write(final String text) {
        this.game.processCommandSpecification(
                new WriteCommandSpecification(this.internalHamster, text));
    }

    /*
     * Queries
     */
    /**
     * Checks the front of the hamster.
     * @return true if the front of the hamster is clear, i.e., the hamster
     *              could execute a move command successfully.
     */
    public /*@ pure @*/ boolean frontIsClear() {
        checkState(this.internalHamster.getCurrentTile().isPresent());
        final LocationVector movementVector = this.internalHamster.getDirection().getMovementVector();
        final Tile currentTile = this.internalHamster.getCurrentTile().get();
        if (currentTile.getLocation().getRow() + movementVector.getDeltaRow() < 0
            || currentTile.getLocation().getColumn() + movementVector.getDeltaColumn() < 0) {
            return false;
        }

        final Location potentialNewLocation = currentTile.getLocation().translate(movementVector);

        if (!currentTile.getTerritory().isLocationInTerritory(potentialNewLocation)) {
            return false;
        }

        return !currentTile.getTerritory().getTileAt(potentialNewLocation).isBlocked();
    }

    /**
     * Checks the hamster's current tile for grain.
     * @return true if there are grain objects available on the hamster's current tile,
     *              i.e., a pickGrain command could execute successfully.
     */
    public /*@ pure @*/ boolean grainAvailable() {
        final Optional<Tile> tile = this.internalHamster.getCurrentTile();
        checkArgument(tile.isPresent());
        return tile.get().getGrainCount() > 0;
    }

    /**
     * Checks whether the hamster's mouth is empty or whether it
     * has grains in its mouth.
     * @return true when there are no grain objects left in the hamster's mouth,
     *              i.e., when a putGrain command can not be executed.
     */
    public /*@ pure @*/ boolean mouthEmpty() {
        return this.internalHamster.getGrainInMouth().isEmpty();
    }

    /**
     * Get the current hamster location.
     * @return The current hamster's location in the territory.
     */
    public /*@ pure @*/ Location getLocation() {
        return this.internalHamster.getCurrentTile().get().getLocation();
    }

    /**
     * Get the current hamster looking direction.
     * @return The current hamster's looking direction.
     */
    public /*@ pure @*/ Direction getDirection() {
        return this.internalHamster.getDirection();
    }

    /** Internal factory object used to create a hamster object from the default
     * hamster of the given territory.
     * @param territory The territory for whose default hamster a game hamster should
     *                  be created.
     * @return The hamster object for the territories default hamster.
     */
    static Hamster fromInternalDefaultHamster(final Territory territory) {
        return new Hamster(territory);
    }

    /** Select a random grain object from the given tile.
     * @param tile The tile to check for grains.
     * @return A random grain object or null if there are no grains on the tile.
     */
    private Grain getRandomGrainFromTile(final Tile tile) {
        final Collection<TileContent> content = tile.getContent();
        for (final TileContent c : content) {
            if (c instanceof Grain) {
                return (Grain) c;
            }
        }
        return null;
    }
}
