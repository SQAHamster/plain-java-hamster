package de.hamstersimulator.objectsfirst.ui.javafx;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.HamsterGameController;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableLogEntry;
import de.hamstersimulator.objectsfirst.datatypes.Mode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.LogManager;

/**
 * Controller for the main game scene
 *
 * Note: The static constructor sets the log level of the javafx.scene.control logger to WARNING to
 * hide a INFO message caused by the log list
 */
public class GameSceneController {

    static {
        final String logLevelString = "javafx.scene.control.level = WARNING";
        final InputStream logLevelStream = new ByteArrayInputStream(logLevelString.getBytes(StandardCharsets.UTF_8));
        final Function<String, BiFunction<String, String, String>> mapper = (category) -> {
            return (oldValue, newValue) -> {
                return Optional.ofNullable(newValue).orElse(oldValue);
            };
        };

        try {
            LogManager.getLogManager().updateConfiguration(logLevelStream, mapper);
        } catch (IOException e) {
            final System.Logger logger = System.getLogger(GameSceneController.class.getName());
            logger.log(System.Logger.Level.INFO, "Failed to update JavaFX Controls log level to INFO");
        }
    }

    class CellFormat extends ListCell<ObservableLogEntry> {
        @Override
        protected void updateItem(final ObservableLogEntry item, final boolean empty) {
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

    public void connectToGame(final HamsterGameViewModel hamsterGameViewModel) {
        this.gameController = hamsterGameViewModel.getGameController();
        this.hamsterGrid.bindToTerritory(hamsterGameViewModel.getTerritory());
        this.play.disableProperty().bind(gameController.modeProperty().isNotEqualTo(Mode.PAUSED));
        this.pause.disableProperty().bind(gameController.modeProperty().isNotEqualTo(Mode.RUNNING));
        this.undo.disableProperty().bind(this.gameController.canUndoProperty().not());
        this.redo.disableProperty().bind(this.gameController.canRedoProperty().not());
        this.speed.valueProperty().bindBidirectional(this.gameController.speedProperty());
        this.log.setCellFactory(list -> new CellFormat());
        hamsterGameViewModel.getLog().logProperty().addListener((ListChangeListener<ObservableLogEntry>) change -> {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                updateLogEntries(hamsterGameViewModel);
            });
        });
    }

    /**
     * Updates the log entries based on the current log entries
     * @param hamsterGameViewModel the ViewModel used to get the log entries
     */
    private void updateLogEntries(final HamsterGameViewModel hamsterGameViewModel) {
        final ObservableList<ObservableLogEntry> logEntries = FXCollections.observableArrayList();
        logEntries.addAll(hamsterGameViewModel.getLog().logProperty());
        this.log.setItems(logEntries);
        final int size = log.getItems().size();
        if (size > 1) {
            Platform.runLater(() -> {
                this.log.scrollTo(size - 1);
            });
        }
    }

}

