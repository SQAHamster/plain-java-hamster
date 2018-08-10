import java.io.Console;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public abstract class SimpleHamsterGame {

    private static final String territoryFile = "territories/example01.ter";

    protected final TerritoryWrapper territory;
    protected final GamefieldWrapper gamefield;
    protected GameHamster paule;
    protected Console console = System.console();

    public SimpleHamsterGame() {
        final CommandStack<Command> editStack = new GameCommandStack<>();
        this.territory = new TerritoryWrapper(new Territory(editStack), territoryFile);
        this.gamefield = new GamefieldWrapper(this.territory);
        paule = GameHamster.fromInternalHamster(territory.getTerritory().getDefaultHamster());
        run();
    }

    abstract void run();


    public static void main(final String[] args) {
        JavaFXUI.start();

        new Example01();
    }
}
