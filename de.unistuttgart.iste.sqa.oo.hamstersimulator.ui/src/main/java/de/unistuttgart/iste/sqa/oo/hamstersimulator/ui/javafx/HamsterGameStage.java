package de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx;

import java.io.IOException;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class HamsterGameStage extends Stage {

    private GameSceneController sceneController;

    public HamsterGameStage(final HamsterGameViewModel hamsterGameViewModel) throws IOException {
        super();
        prepareStage();
        sceneController.connectToGame(hamsterGameViewModel);
        this.setOnCloseRequest(event -> {
            final HamsterGameController gameController = hamsterGameViewModel.getGameController();
            new Thread(gameController::abortOrStopGame).start();
        });
    }

    public void prepareStage() throws IOException {
        this.setTitle("Hamster Simulator - Game Window");
        final BorderPane root = (BorderPane) loadFromFXML();
        final Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add("de/unistuttgart/iste/sqa/oo/hamstersimulator/ui/ressources/css/game.css");
        this.minHeightProperty().bind(root.minHeightProperty());
        this.minWidthProperty().bind(root.minWidthProperty());
        this.setScene(scene);
    }

    private Parent loadFromFXML() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader()
                .getResource("de/unistuttgart/iste/sqa/oo/hamstersimulator/ui/ressources/fxml/GameScene.fxml"));
        final Parent root = fxmlLoader.load();
        this.sceneController = (GameSceneController) fxmlLoader.getController();
        return root;
    }
}
