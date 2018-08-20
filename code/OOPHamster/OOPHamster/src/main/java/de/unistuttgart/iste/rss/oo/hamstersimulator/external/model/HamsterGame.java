package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class HamsterGame {

    private static final String DEFAULT_HAMSTER_TERRITORY = "territories/example01.ter";

    private final GameLog log = new GameLog();
    private final GameCommandStack commandStack = new GameCommandStack();
    private final Territory territory = new Territory(this);

    public Territory getTerritory() {
        return territory;
    }
    
    public GameLog getGameLog() {
        return log;
    }

    public void initialize() {
        initialize(DEFAULT_HAMSTER_TERRITORY);
    }

    public void initialize(final String territoryFile) {
        new EditCommandStack().execute(TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromFile(territoryFile));
    }

    public void reset() {
        this.commandStack.undoAll();
        this.commandStack.reset();
    }

    public void displayInNewGameWindow() {
        JavaFXUI.openSceneFor(this, this.territory.getInternalTerritory(), this.commandStack);
    }

    public void runGame(final Consumer<Territory> hamsterProgram) {
        this.commandStack.startGame();
        hamsterProgram.accept(this.territory);
        this.commandStack.stopGame();
    }

    public void finished() {
        this.commandStack.stopGame();
    }

    public void processCommandSpecification(final CommandSpecification specification) {
        final Command territoryCommandPart = this.getTerritory().getInternalTerritory().getCommandFromSpecification(specification);
        final Command logCommandPart = this.log.getCommandFromSpecification(specification);
        final Command composite = new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.add(territoryCommandPart, logCommandPart);
            }
        };
        this.commandStack.execute(composite);
    }
}
