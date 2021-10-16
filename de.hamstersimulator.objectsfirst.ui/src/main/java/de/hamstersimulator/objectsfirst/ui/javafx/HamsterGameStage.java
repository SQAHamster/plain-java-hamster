package de.hamstersimulator.objectsfirst.ui.javafx;

import java.io.IOException;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.HamsterGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HamsterGameStage extends Stage {

    private GameSceneController sceneController;
    private final HamsterGameViewModel hamsterGameViewModel;

    public HamsterGameStage(final HamsterGameViewModel hamsterGameViewModel) throws IOException {
        super();
        this.hamsterGameViewModel = hamsterGameViewModel;
        prepareStage();
        sceneController.connectToGame(hamsterGameViewModel);
        this.setOnCloseRequest(event -> this.onCloseRequest());
    }

    public void prepareStage() throws IOException {
        this.setTitle("Hamster Simulator - Game Window");
        final BorderPane root = (BorderPane) loadFromFXML();
        final Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add("de/hamstersimulator/objectsfirst/ui/ressources/css/game.css");
        this.minHeightProperty().bind(root.minHeightProperty());
        this.minWidthProperty().bind(root.minWidthProperty());
        this.setScene(scene);
    }

    private Parent loadFromFXML() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader()
                .getResource("de/hamstersimulator/objectsfirst/ui/ressources/fxml/GameScene.fxml"));
        final Parent root = fxmlLoader.load();
        this.sceneController = (GameSceneController) fxmlLoader.getController();
        return root;
    }

    protected void onCloseRequest() {
        final HamsterGameController gameController = hamsterGameViewModel.getGameController();
        new Thread(gameController::abortOrStopGame).start();
    }
}
