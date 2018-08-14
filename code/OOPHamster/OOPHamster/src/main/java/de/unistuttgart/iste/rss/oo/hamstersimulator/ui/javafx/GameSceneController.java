package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
    @FXML private Pane hamsterPane;
    private Territory territory;
    private GameCommandStack<Command> commandStack;

    @FXML
    private void initialize() {
        final DoubleBinding prop = hamsterGrid.minHeightProperty().add(toolbar.heightProperty()).add(30);
        this.root.minHeightProperty().bind(prop);
        this.root.minWidthProperty().bind(hamsterGrid.minWidthProperty());
        this.splitPane.setDividerPositions(0.3f, 0.6f, 0.9f);
        this.hamsterPane.minWidthProperty().bind(this.hamsterGrid.minWidthProperty());
        this.hamsterPane.minHeightProperty().bind(this.hamsterGrid.minHeightProperty());
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

    public void connectToTerritory(final Territory territory) {
        this.hamsterGrid.bindToTerritory(territory);
        this.territory = territory;
        this.commandStack = ((GameCommandStack<Command>)this.territory.getCommandStack());
        this.play.disableProperty().bind(Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.PAUSED, commandStack.stateProperty()).not());
        this.pause.disableProperty().bind(Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.RUNNING, commandStack.stateProperty()).not());
        this.undo.disableProperty().bind(this.commandStack.canUndoProperty().not());
        this.redo.disableProperty().bind(this.commandStack.canRedoProperty().not());
        this.speed.valueProperty().bindBidirectional(this.commandStack.speedProperty());
    }

}

