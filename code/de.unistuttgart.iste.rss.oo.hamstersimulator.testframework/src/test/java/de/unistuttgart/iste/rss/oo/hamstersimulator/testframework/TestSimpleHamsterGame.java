package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

public class TestSimpleHamsterGame extends SimpleHamsterGame {

    public TestSimpleHamsterGame() {
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(new Size(6, 6));
        builder.defaultHamsterAt(Location.from(0, 0), Direction.SOUTH, 0);
        game.initialize(builder);
        game.startGame();
    }

    @Override
    protected void run() {
        for (int i = 0; i < 5; i++) {
            paule.move();
        }
    }
}
