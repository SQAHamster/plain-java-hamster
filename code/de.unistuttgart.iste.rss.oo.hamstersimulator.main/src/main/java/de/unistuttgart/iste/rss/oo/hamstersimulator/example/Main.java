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

        builder.initializeTerritory(new Size(4, 3));
        builder.defaultHamsterAt(Location.ORIGIN, Direction.EAST, 0);
        builder.grainAt(Location.from(0, 2));
        
        return builder;
    }

}
