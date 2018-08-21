package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.Optional;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.AbstractHamsterCommandSpecification;
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
        final Optional<Command> territoryCommandPart = this.getTerritory().getInternalTerritory().getCommandFromSpecification(specification);
        final Optional<Command> logCommandPart = this.log.getCommandFromSpecification(specification);
        final Optional<Command> hamsterPart;
        if (specification instanceof AbstractHamsterCommandSpecification) {
            final AbstractHamsterCommandSpecification hamsterCommandSpec = (AbstractHamsterCommandSpecification) specification;
            hamsterPart = hamsterCommandSpec.getHamster().getCommandFromSpecification(specification);
        } else {
            hamsterPart = Optional.empty();
        }
        final Command composite = new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                territoryCommandPart.ifPresent(c -> builder.add(c));
                logCommandPart.ifPresent(c -> builder.add(c));
                hamsterPart.ifPresent(c -> builder.add(c));
            }
        };
        this.commandStack.execute(composite);
    }
}
