package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class Territory {

    private final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory internalTerritory;
    private final Hamster defaultHamster;

    public Territory(final CommandStack<Command> stack) {
        super();
        this.internalTerritory = new de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory(stack);
        this.defaultHamster = Hamster.fromInternalHamster(this.internalTerritory.getDefaultHamster());
    }

    Territory(final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory internalTerritory) {
        super();
        this.internalTerritory = internalTerritory;
        this.defaultHamster = Hamster.fromInternalHamster(this.internalTerritory.getDefaultHamster());
    }

    de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory getInternalTerritory() {
        return this.internalTerritory;
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public void cloneInto(final Territory target) {
        target.getInternalTerritory().getCommandStack().execute(
                ((EditCommandStack<Command>)this.internalTerritory.getCommandStack()).cloneCommandsInto(target.internalTerritory));
    }

    public void reset() {
        this.internalTerritory.getCommandStack().undoAll();
    }

    public void displayInNewGameWindow() {
        JavaFXUI.openSceneFor(this.internalTerritory);
    }

    public void loadFromFile(final String territoryFile) {
        TerritoryLoader.initializeFor(this.internalTerritory).loadFromFile(territoryFile);
    }

    public void runGame(final Consumer<Territory> hamsterProgram) {
        final GameCommandStack<Command> gameStack = (GameCommandStack<Command>) this.internalTerritory.getCommandStack();
        gameStack.startGame();
        hamsterProgram.accept(this);
        gameStack.stopGame();
        gameStack.reset();
    }

    protected static void delay(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (final InterruptedException e) { }
    }
}
