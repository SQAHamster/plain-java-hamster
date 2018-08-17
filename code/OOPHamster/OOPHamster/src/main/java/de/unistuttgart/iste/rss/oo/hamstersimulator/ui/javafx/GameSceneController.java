package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class GameSceneController {

    @FXML private BorderPane root;
    @FXML private ToolBar toolbar;
    @FXML private Button play;
    @FXML private Button pause;
    @FXML private Button undo;
    @FXML private Button redo;
    @FXML private Slider speed;
    @FXML private HamsterTerritoryGrid hamsterGrid;
    @FXML private SplitPane splitPane;
    @FXML private ListView<String> log;

    private GameCommandStack<Command> commandStack;
    private HamsterGame game;

    @FXML
    private void initialize() {
    }

    @FXML
    void pauseGame(final ActionEvent event) {
        commandStack.pause();
    }

    @FXML
    void undo(final ActionEvent event) {
        commandStack.undo();
    }

    @FXML
    void redo(final ActionEvent event) {
        commandStack.redo();
    }

    @FXML
    void startGame(final ActionEvent event) {
        commandStack.resume();
    }

    public void connectToGame(final HamsterGame hamsterGame) {
        this.game = hamsterGame;
        this.hamsterGrid.bindToTerritory(game.getInternalTerritory());
        this.commandStack = game.getCommandStack();
        final BooleanBinding runningBinding = Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.RUNNING, commandStack.stateProperty());
        this.play.disableProperty().bind(Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.PAUSED, commandStack.stateProperty()).not());
        this.pause.disableProperty().bind(runningBinding.not());
        this.undo.disableProperty().bind(this.commandStack.canUndoProperty().not().or(runningBinding));
        this.redo.disableProperty().bind(this.commandStack.canRedoProperty().not().or(runningBinding));
        this.speed.valueProperty().bindBidirectional(this.commandStack.speedProperty());
        this.log.itemsProperty().bind(this.game.logProperty());
    }

}

