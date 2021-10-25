package de.hamstersimulator.objectsfirst.testframework;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Size;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.external.model.TerritoryBuilder;

/**
 * Basic SimpleHamsterGame which creates a 6x6 territory with the Hamster at 0,0 facing south.
 */
class TestSimpleHamsterGame extends SimpleHamsterGame {

    /**
     * Size of the territory in this test territory.
     */
    private static final Size TEST_TERRITORY_SIZE = new Size(6, 6);

    /**
     * Creates a new TestSimpleHamsterGame, creates a 6x6 territory with the Hamster at 0,0 facing south.
     * Starts the game, but adds no InputInterface.
     */
    TestSimpleHamsterGame() {
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(TEST_TERRITORY_SIZE);
        builder.defaultHamsterAt(Location.ORIGIN, Direction.SOUTH, 0);
        game.initialize(builder);
        game.startGame();
    }

    /**
     * moves paule to the bottom left corner.
     */
    @Override
    protected void run() {
        while (paule.frontIsClear()) {
            paule.move();
        }
    }
}
