package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

/**
 * Class demonstrating the intended use of the hamster simulator API.
 * @author Steffen Becker
 *
 */
public final class Main {

    /**
     * A constant containing the location of an example grain on the territory.
     */
    private static final Location GRAIN_LOCATION = Location.from(1, 3);

    /**
     * Row count of the territory.
     */
    private static final int MAX_ROW_COUNT = 3;

    /**
     * Column count of the territory.
     */
    private static final int MAX_COLUMN_COUNT = 5;

    /**
     * Size of the example's territory.
     */
    private static final Size EXAMPLE_TERRITORY_SIZE = new Size(MAX_ROW_COUNT, MAX_COLUMN_COUNT);

    /**
     * Main routine of the example class.
     * @param args Command line arguments, not used here.
     */
    public static void main(final String...args) {
        final HamsterGame hamsterGame = new HamsterGame();

        hamsterGame.initialize(createTerritory(hamsterGame));
        initUI(hamsterGame);
        runGameCycle(hamsterGame);
    }

    /**
     * Starts, runs and finally stops the hamster game.
     * @param hamsterGame The hamster game to run, must not be null.
     */
    private static void runGameCycle(final HamsterGame hamsterGame) {
        assert hamsterGame != null;

        hamsterGame.startGame(false);
        hamsterGame.runGame(Main::runHamster);
        hamsterGame.stopGame();
    }

    /** Setup routine which configures the UI appropriately.
     * @param hamsterGame The hamster game to run, must not be null.
     */
    private static void initUI(final HamsterGame hamsterGame) {
        assert hamsterGame != null;

        JavaFXUI.displayInNewGameWindow(hamsterGame.getModelViewAdapter());
    }

    /** This operation captures the algorithm executed by the hamster.
     * @param territory The territory of the example. It has to contain
     *                  the default hamster. Must not be null.
     */
    private static void runHamster(final Territory territory) {
        assert territory != null;

        final Hamster paule = territory.getDefaultHamster();
        paule.move();
        paule.move();
        paule.pickGrain();
    }

    /**
     * This operation configures a hamster territory via API. It is a demo
     * of the territory builder class.
     * @param hamsterGame The hamster game to run, must not be null.
     * @return A {@see TerritoryBuilder} object containing the information how to
     *         initialize this example's territory.
     */
    private static TerritoryBuilder createTerritory(final HamsterGame hamsterGame) {
        assert hamsterGame != null;

        final TerritoryBuilder builder = hamsterGame.getNewTerritoryBuilder();

        builder.initializeTerritory(EXAMPLE_TERRITORY_SIZE);
        builder.defaultHamsterAt(Location.from(1, 1), Direction.EAST, 0);
        builder.grainAt(GRAIN_LOCATION);

        createWall(builder, Location.ORIGIN, Location.from(0, MAX_COLUMN_COUNT - 1));
        createWall(builder, Location.from(1, 0), Location.from(MAX_ROW_COUNT - 2, 0));
        createWall(builder, Location.from(1, MAX_COLUMN_COUNT - 1),
                        Location.from(MAX_ROW_COUNT - 2, MAX_COLUMN_COUNT - 1));
        createWall(builder, Location.from(MAX_ROW_COUNT - 1, 0),
                        Location.from(MAX_ROW_COUNT - 1, MAX_COLUMN_COUNT - 1));

        return builder;
    }

    /**
     * Helper operation to create a bunch of walls in a given rectangular area.
     * @param builder The territory builder to use to place the wall.
     * @param from Upper, left corner of the rectangle.
     * @param to Lower, right corner of the rectangle.
     */
    private static void createWall(final TerritoryBuilder builder, final Location from, final Location to) {
        assert builder != null;
        assert from != null;
        assert to != null;
        Location.getAllLocationsFromTo(from, to).forEach(location -> builder.wallAt(location));
    }

    /**
     * Private constructor preventing instances of this class.
     */
    private Main() { }

}
