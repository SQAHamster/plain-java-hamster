package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

/**
 * Basic SimpleHamsterGame which creates a 6x6 territory with the Hamster at 0,0 facing south.
 */
public final class TestSimpleHamsterGame extends SimpleHamsterGame {

    /**
     * Inital grain count of paule in this scenario.
     */
    private static final int PAULES_INITIAL_GAIN_COUNT = 10;

    /**
     * Size of the territory in this test territory.
     */
    private static final Size TEST_TERRITORY_SIZE = new Size(6, 6);

    /**
     * Creates a new TestSimpleHamsterGame, creates a 6x6 territory with the Hamster at 0,0 facing south.
     * Starts the game, but adds no InputInterface.
     */
    public TestSimpleHamsterGame() {
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(TEST_TERRITORY_SIZE);
        builder.defaultHamsterAt(Location.ORIGIN, Direction.SOUTH, PAULES_INITIAL_GAIN_COUNT);
        game.initialize(builder);
        game.startGame();
    }

    /**
     * moves paule to the bottom left corner.
     */
    @Override
    protected void run() {
        while (paule.frontIsClear()) {
            paule.putGrain();
            paule.move();
        }
        paule.write("Done placing grains");
        final Hamster paula = new Hamster(this.game.getTerritory(), Location.ORIGIN, Direction.SOUTH, 0);
        while (paula.frontIsClear()) {
            if (paula.grainAvailable()) {
                paula.pickGrain();
            }
            paula.move();
        }
    }
}
