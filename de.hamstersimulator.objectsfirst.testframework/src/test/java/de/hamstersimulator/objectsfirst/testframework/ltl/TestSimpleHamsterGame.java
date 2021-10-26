package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Size;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.external.model.TerritoryBuilder;

/**
 * Basic SimpleHamsterGame which creates a 6x6 territory with the Hamster at 0,0 facing south.
 */
public final class TestSimpleHamsterGame extends SimpleHamsterGame {

    /**
     * Initial grain count of paule in this scenario.
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
