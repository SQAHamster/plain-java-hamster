package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

/**
 * Basic SimpleHamsterGame which creates a 6x6 territory with the Hamster at 0,0 facing south
 */
class TestSimpleHamsterGame extends SimpleHamsterGame {

    /**
     * Creates a new TestSimpleHamsterGame, creates a 6x6 territory with the Hamster at 0,0 facing south
     * Starts the game, but adds no InputInterface
     */
    public TestSimpleHamsterGame() {
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(new Size(6, 6));
        builder.defaultHamsterAt(Location.from(0, 0), Direction.SOUTH, 0);
        game.initialize(builder);
        game.startGame();
    }

    /**
     * moves paule to the bottom left corner
     */
    @Override
    protected void run() {
        for (int i = 0; i < 5; i++) {
            paule.move();
        }
    }
}
