package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameController;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableLogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class GameSceneController {

    class CellFormat extends ListCell<ObservableLogEntry> {
        @Override
        protected void updateItem(final ObservableLogEntry item, final boolean empty) {
            Platform.runLater(() -> {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMessage());
                    if (hamsterGrid.hamsterToColorPos.containsKey(item.getHamster())) {
                        setTextFill(TileNode.hamsterColors[hamsterGrid.hamsterToColorPos.get(item.getHamster())]);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            });
        }
    }

    @FXML private BorderPane root;
    @FXML private ToolBar toolbar;
    @FXML private Button play;
    @FXML private Button pause;
    @FXML private Button undo;
    @FXML private Button redo;
    @FXML private Slider speed;
    @FXML private HamsterTerritoryGrid hamsterGrid;
    @FXML private SplitPane splitPane;
    @FXML private ListView<ObservableLogEntry> log;

    private HamsterGameController gameController;

    @FXML
    private void initialize() {
    }

    @FXML
    void pauseGame(final ActionEvent event) {
        new Thread(gameController::pauseAsync).start();
    }

    @FXML
    void undo(final ActionEvent event) {
        new Thread(gameController::undo).start();
    }

    @FXML
    void redo(final ActionEvent event) {
        new Thread(gameController::redo).start();
    }

    @FXML
    void startGame(final ActionEvent event) {
        new Thread(gameController::resume).start();
    }

    @SuppressWarnings("unchecked")
    public void connectToGame(final HamsterGameViewModel hamsterGameViewModel) {
        this.gameController = hamsterGameViewModel.getGameController();
        this.hamsterGrid.bindToTerritory(hamsterGameViewModel.getTerritory());
        this.play.disableProperty().bind(gameController.modeProperty().isNotEqualTo(Mode.PAUSED));
        this.pause.disableProperty().bind(gameController.modeProperty().isNotEqualTo(Mode.RUNNING));
        this.undo.disableProperty().bind(this.gameController.canUndoProperty().not());
        this.redo.disableProperty().bind(this.gameController.canRedoProperty().not());
        this.speed.valueProperty().bindBidirectional(this.gameController.speedProperty());
        this.log.setCellFactory(list -> new CellFormat());
        this.log.itemsProperty().bind((ReadOnlyListProperty<ObservableLogEntry>) hamsterGameViewModel.getLog().logProperty());
        this.log.getItems().addListener((ListChangeListener<ObservableLogEntry>) changeListener -> {
            changeListener.next();
            final int size = log.getItems().size();
            if (size > 1) {
                JavaFXUtil.blockingExecuteOnFXThread(() -> {
                    final Parent virtualFlow = (Parent) log.getChildrenUnmodifiable().get(0);
                    final Parent group = (Parent) virtualFlow.getChildrenUnmodifiable().get(1);
                    final Parent cell = (Parent) group.getChildrenUnmodifiable().get(0);
                    final ListCell<ObservableLogEntry> listCell = (ListCell<ObservableLogEntry>) cell;
                    final int visibleCells = (int) (log.getHeight() / listCell.getHeight());
                    log.scrollTo(Math.max(0, size - visibleCells));
                });
            }
        });
    }

}

