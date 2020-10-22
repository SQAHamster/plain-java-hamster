package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

/**
 * Example demonstrating the usage of the TerritoryBuilder
 * Creates a new territory programmatically and loads it
 */
public class Example05 extends SimpleHamsterGame {

    /**
     * Creates a new instance of Example05
     */
    public Example05() {
        game.initialize(createTerritory());
        game.startGame(false);
        displayInNewGameWindow();
    }

    @Override
    protected void run() {

    }

    /**
     * This operation configures a hamster territory via API. It is a demo
     * of the territory builder class.
     * @return A {@see TerritoryBuilder} object containing the information how to
     *         initialize this example's territory.
     */
    private TerritoryBuilder createTerritory() {
        final TerritoryBuilder builder = this.game.getNewTerritoryBuilder();

        builder.initializeTerritory(new Size(5, 6));
        builder.defaultHamsterAt(Location.from(1, 1), Direction.EAST, 0);
        builder.grainAt(new Location(4,5));
        builder.wallAt(Location.from(3,3));

        Location.getAllLocationsFromTo(Location.from(0, 0), Location.from(0, 5))
                .forEach(builder::wallAt);

        return builder;
    }
}
