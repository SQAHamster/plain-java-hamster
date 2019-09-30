package de.unistuttgart.iste.rss.oo.hamstersimulator.example;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class Main {

    private static final int MAX_ROW_COUNT = 3;
    private static final int MAX_COLUMN_COUNT = 5;
    private static final Size EXAMPLE_TERRITORY_SIZE = new Size(MAX_ROW_COUNT, MAX_COLUMN_COUNT);

    public static void main(final String[] args) {
        final HamsterGame hamsterGame = new HamsterGame();

        hamsterGame.initialize(createTerritory(hamsterGame));
        initUI(hamsterGame);
        runGameCycle(hamsterGame);
    }

    public static void runGameCycle(final HamsterGame hamsterGame) {
        hamsterGame.startGame(false);
        hamsterGame.runGame(Main::runHamster);
        hamsterGame.stopGame();
    }

    public static void initUI(final HamsterGame hamsterGame) {
        JavaFXUI.start();
        hamsterGame.displayInNewGameWindow();
        hamsterGame.setInputInterface(JavaFXUI.getJavaFXInputInterface());
    }

    public static void runHamster(final Territory territory) {
        final Hamster paule = territory.getDefaultHamster();
        paule.move();
        paule.move();
        paule.pickGrain();
    }

    private static TerritoryBuilder createTerritory(final HamsterGame hamsterGame) {
        final TerritoryBuilder builder = hamsterGame.getNewTerritoryBuilder();

        builder.initializeTerritory(EXAMPLE_TERRITORY_SIZE);
        builder.defaultHamsterAt(Location.from(1, 1), Direction.EAST, 0);
        builder.grainAt(Location.from(1, 3));

        createWall(builder, Location.ORIGIN, Location.from(0, MAX_COLUMN_COUNT-1));
        createWall(builder, Location.from(1, 0), Location.from(MAX_ROW_COUNT-2, 0));
        createWall(builder, Location.from(1, MAX_COLUMN_COUNT-1), Location.from(MAX_ROW_COUNT-2, MAX_COLUMN_COUNT-1));
        createWall(builder, Location.from(MAX_ROW_COUNT-1, 0), Location.from(MAX_ROW_COUNT-1, MAX_COLUMN_COUNT-1));

        return builder;
    }

    private static void createWall(final TerritoryBuilder builder, final Location from, final Location to) {
        Location.getAllLocationsFromTo(from, to).forEach(location -> builder.wallAt(location));
    }

}
