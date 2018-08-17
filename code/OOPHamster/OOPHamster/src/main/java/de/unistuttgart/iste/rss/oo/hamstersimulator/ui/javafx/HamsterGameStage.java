package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class HamsterGameStage extends Stage {

    private GameSceneController sceneController;

    public HamsterGameStage(final HamsterGame hamsterGame) throws IOException {
        super();
        prepareStage();
        sceneController.connectToGame(hamsterGame);
    }

    public void prepareStage() throws IOException {
        this.setTitle("Hamster Simulator - Game Window");
        final BorderPane root = (BorderPane) loadFromFXML();
        final Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add("css/game.css");
        this.minHeightProperty().bind(root.minHeightProperty());
        this.minWidthProperty().bind(root.minWidthProperty());
        this.setScene(scene);
    }

    private Parent loadFromFXML() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/GameScene.fxml"));
        final Parent root = fxmlLoader.load();
        this.sceneController = (GameSceneController) fxmlLoader.getController();
        return root;
    }
}
