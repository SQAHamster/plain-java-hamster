import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class Main {

    public static void main(final String[] args) {
        JavaFXUI.start();

        final HamsterGame game = new HamsterGame();
        game.initialize();

        game.displayInNewGameWindow();
        game.runGame(Main::exampleRun);
    }

    public static void exampleRun(final Territory territory) {
        final Hamster paule = territory.getDefaultHamster();
        final Hamster willi = new Hamster(territory, Location.from(1, 3), Direction.WEST, 0);
        final Hamster marry = new Hamster(territory, Location.from(1, 2), Direction.EAST, 0);

        while (!paule.grainAvailable() && paule.frontIsClear()) {
            paule.move();
        }
        while (paule.grainAvailable()) {
            paule.pickGrain();
        }
        paule.turnLeft();
        paule.turnLeft();
        while (paule.frontIsClear()) {
            paule.move();
        }
        while (!paule.mouthEmpty()) {
            paule.putGrain();
        }
        willi.move();
        marry.move();
    }
}
