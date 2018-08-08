import java.io.IOException;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.GameCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class Main {

    public static void main(final String[] args) throws IOException, CloneNotSupportedException {
        JavaFXUI.start();

        final String territoryFile = "/Users/snowball/test.ter";
        final CommandStack<CommandInterface> editStack = new CommandStack<>();
        final Territory territory = new Territory(editStack);
        final CommandStack<GameCommand> gameStack = new CommandStack<>();

        JavaFXUI.getSingleton().init(territory);

        TerritoryLoader.initializeFor(territory).loadFromFile(territoryFile);

        delay(1000);
        exampleRun(territory, gameStack);
        delay(2000);
        gameStack.undoAll();
    }

    protected static void delay(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (final InterruptedException e) { }
    }

    private static void exampleRun(final Territory territory, final CommandStack<GameCommand> commandStack) {
        final GameHamster paule = territory.getDefaultHamster();
        final GameHamster willi = new GameHamster();
        commandStack.execute(willi.getInitializeHamsterCommand(Optional.of(territory), Optional.of(Location.from(1, 3)), Direction.WEST, 0));
        final GameHamster marry = new GameHamster();
        commandStack.execute(marry.getInitializeHamsterCommand(Optional.of(territory), Optional.of(Location.from(1, 2)), Direction.EAST, 0));

        while (!paule.grainAvailable() && paule.frontIsClear()) {
            paule.move(commandStack);
        }
        while (paule.grainAvailable()) {
            paule.pickGrain(commandStack);
        }
        paule.turnLeft(commandStack);
        paule.turnLeft(commandStack);
        while (paule.frontIsClear()) {
            paule.move(commandStack);
        }
        while (!paule.mouthEmpty()) {
            paule.putGrain(commandStack);
        }
        willi.move(commandStack);
        marry.move(commandStack);
    }
}
