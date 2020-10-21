package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

import java.io.IOException;

public class TestHamsterGame extends SimpleHamsterGame {

    private static void createWall(final TerritoryBuilder builder, final Location from, final Location to) {
        assert builder != null;
        assert from != null;
        assert to != null;
        Location.getAllLocationsFromTo(from, to).forEach(location -> builder.wallAt(location));
    }

    public TestHamsterGame() {
        try {
            // game.initialize("/territories/empty.ter");
            this.game.initialize("/territories/example01.ter");
            this.game.startGame(false);
            //game.connectToHamsterServer();
            // game.initialize("/territories/empty.ter");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    @Override
    protected void run() {
        System.out.println("Run");
        System.out.println(this.paule.readNumber("Hi"));
        this.paule.move();
        this.paule.move();
        this.paule.pickGrain();
        this.paule.turnLeft();
        this.paule.turnLeft();
        this.paule.move();
        this.paule.move();
        this.paule.turnLeft();
        this.paule.turnLeft();
        this.paule.move();
        this.paule.move();
        this.paule.turnLeft();
    }
}
