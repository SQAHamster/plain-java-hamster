import java.io.Console;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public abstract class SimpleHamsterGame {

    protected HamsterGame game = new HamsterGame();
    protected Hamster paule;
    protected Console console = System.console();

    protected void start() {
        paule = game.getTerritory().getDefaultHamster();
        
        run();
        game.finished();
    }

    abstract void run();

    public static void main(final String[] args) {
        JavaFXUI.start();

        new Example01().start();
    }
}
