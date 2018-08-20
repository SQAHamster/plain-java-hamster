package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkNotNull;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.WriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;

public class Hamster {

    private HamsterGame game;
    private final GameHamster internalHamster;

    public Hamster() {
        super();
        this.internalHamster = new GameHamster();
    }

    public Hamster(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        this();
        init(territory, location, newDirection, newGrainCount);
    }

    private Hamster(final Territory territory) {
        super();
        this.game = territory.getGame();
        this.internalHamster = territory.getInternalTerritory().getDefaultHamster();
    }

    /*
     * Commands
     */
    public void init(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        checkNotNull(territory);
        checkNotNull(location);
        checkNotNull(newDirection);
        checkArgument(newGrainCount >= 0);
        this.game = territory.getGame();
        this.game.processCommandSpecification(new InitHamsterCommandSpecification(territory.getInternalTerritory(), location, newDirection, newGrainCount));
    }

    public void move() {
        this.game.processCommandSpecification(new MoveCommandSpecification(this.internalHamster));
    }

    public void turnLeft() {
        this.game.processCommandSpecification(new TurnLeftCommandSpecification(this.internalHamster));
    }

    public void pickGrain() {
        final Grain grain = (Grain) this.internalHamster.getCurrentTile().get().getContent().stream().filter(t -> t instanceof Grain).findFirst().get();
        this.game.processCommandSpecification(new PickGrainCommandSpecification(this.internalHamster, grain));
    }

    public void putGrain() {
        this.game.processCommandSpecification(new PutGrainCommandSpecification(this.internalHamster, this.internalHamster.getGrainInMouth().get(0)));
    }

    public void readNumber() {
        // TODO - implement Hamster.readNumber
    }

    public void readString() {
        // TODO - implement Hamster.readString
    }

    public void write(final String text) {
        this.game.processCommandSpecification(new WriteCommandSpecification(this.internalHamster, text));
    }

    /*
     * Queries
     */
    public boolean frontIsClear() {
        final LocationVector movementVector = this.internalHamster.getDirection().getMovementVector();
        final Tile currentTile = this.internalHamster.getCurrentTile().orElseThrow(IllegalArgumentException::new);
        final Location potentialNewLocation = currentTile.getLocation().translate(movementVector);

        if (!currentTile.getTerritory().isLocationInTerritory(potentialNewLocation)) {
            return false;
        }

        return !currentTile.getTerritory().getTileAt(potentialNewLocation).isBlocked();
    }

    public boolean grainAvailable() {
        return this.internalHamster.getCurrentTile().orElseThrow(IllegalStateException::new).getGrainCount() > 0;
    }

    public boolean mouthEmpty() {
        return this.internalHamster.getGrainInMouth().isEmpty();
    }

    public static Hamster fromInternalDefaultHamster(final Territory territory) {
        return new Hamster(territory);
    }

}
