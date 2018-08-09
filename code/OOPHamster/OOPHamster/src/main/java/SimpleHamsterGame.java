import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public abstract class SimpleHamsterGame {

    private static final String territoryFile = "territories/example01.ter";

    protected final Territory territory;
    protected GameHamster paule;

    public SimpleHamsterGame() {
        final CommandStack<AbstractBaseCommand<?>> editStack = new CommandStack<>();
        this.territory = new Territory(editStack);
        run();
    }

    abstract void run();

    void showGamefield() {
        JavaFXUI.getSingleton().init(territory);
    }

    void initializeTerritory() {
        TerritoryLoader.initializeFor(territory).loadFromFile(territoryFile);
        paule = GameHamster.fromInternalHamster(territory.getDefaultHamster());
    }

    public static void main(final String[] args) {
        JavaFXUI.start();

        new Example01();
    }
}
