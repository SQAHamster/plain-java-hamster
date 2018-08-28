import java.io.Console;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public abstract class SimpleHamsterGame {

    protected HamsterGame game = new HamsterGame();
    protected Hamster paule;
    protected Console console = System.console();

    public SimpleHamsterGame() {
        JavaFXUI.start();

        game.initialize();
        game.displayInNewGameWindow();
        
        paule = game.getTerritory().getDefaultHamster();
    }

    protected void stop() {
        game.finished();
    }

    void run() {}

    public Hamster getPaule() {
        return paule;
    }
    
    public static void main(final String[] args) {
        final Example01 example01 = new Example01();
        example01.run();
        example01.stop();
    }
}
