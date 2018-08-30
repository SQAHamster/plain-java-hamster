package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.GameTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;

/**
 * The territroy class represents territories for hamsters. Territories
 * are rectangular areas with width and height composed out of territory
 * tiles. Tiles again can contain hamster objects, grain objects, or walls.
 * This class provides methods to load or build a territory and to query its
 * current state.
 * @author Steffen Becker
 *
 */
public class Territory {

    /**
     * The parent hamste game to which this territory belongs to.
     */
    private final HamsterGame hamsterGame;

    /**
     * Internal model representation of the territory.
     */
    private final GameTerritory internalTerritory;

    /**
     * Reference to the default hamster of this territory.
     */
    private final Hamster defaultHamster;

    /**
     * Initialize a new territory and link it to its game object.
     * @param game The game in which this territory object is used.
     */
    public Territory(final HamsterGame game) {
        super();
        this.hamsterGame = game;
        this.internalTerritory = new GameTerritory();
        this.defaultHamster = Hamster.fromInternalDefaultHamster(this);
    }

    /**
     * Gets the default hamster of this territory. A default hamster always
     * exists on a territory, so this method never returns null.
     * @return The hamster object representing the default hamster of the
     *         territory.
     */
    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    /**
     * Gets the hamster game associated to this territory object.
     * @return The hamstet game associated to this territory. The default hamster
     *         object is never null.
     */
    public HamsterGame getGame() {
        return this.hamsterGame;
    }

    /**
     * For a given location, tests whether the location is inside the bound of the territory.
     * @param location The location to test for.
     * @return true if the location is inside the territory.
     */
    public /*@ pure @*/ boolean isLocationInTerritory(final Location location) {
        return this.internalTerritory.isLocationInTerritory(location);
    }

    /**
     * Loads the territory from a file. The territory is initalized according to the loaded territory
     * file and the default hamster is initialized.
     * @param territoryFile The territory file path from where to load the territory. The path has to be relative
     *                      to the class path of this class and is loaded via the classes' class loader.
     */
    public void loadFromFile(final String territoryFile) {
        TerritoryLoader.initializeFor(this.internalTerritory).loadFromFile(territoryFile);
    }

    /**
     * Get the internal representation of the territory.
     * @return The internal territory object. This object is never null.
     */
    GameTerritory getInternalTerritory() {
        return this.internalTerritory;
    }

}
