package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;

public class HamsterGame {

    private static final String DEFAULT_HAMSTER_TERRITORY = "territories/example01.ter";

    private final GameCommandStack<Command> commandStack = new GameCommandStack<Command>();
    private final Territory territory = new Territory(this);
    final ReadOnlyListWrapper<LogEntry> gameLog = new ReadOnlyListWrapper<LogEntry>(this, "log", FXCollections.observableArrayList());

    public GameTerritory getInternalTerritory() {
        return territory.getInternalTerritory();
    }

    public Territory getTerritory() {
        return territory;
    }

    public ReadOnlyListProperty<LogEntry> logProperty() {
        return this.gameLog.getReadOnlyProperty();
    }

    public void initialize() {
        initialize(DEFAULT_HAMSTER_TERRITORY);
        //this.getCommandStack().startGame();
    }

    public void initialize(final String territoryFile) {
        new EditCommandStack<Command>().execute(TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromFile(territoryFile));
    }

    public GameCommandStack<Command> getCommandStack() {
        return commandStack;
    }

    public void reset() {
        this.getCommandStack().undoAll();
    }

    public void displayInNewGameWindow() {
        JavaFXUI.openSceneFor(this);
    }

    public void runGame(final Consumer<Territory> hamsterProgram) {
        final GameCommandStack<Command> gameStack = this.getCommandStack();
        gameStack.startGame();
        hamsterProgram.accept(this.territory);
        gameStack.stopGame();
        gameStack.reset();
    }

    public void finished() {
        this.commandStack.stopGame();
    }

}
