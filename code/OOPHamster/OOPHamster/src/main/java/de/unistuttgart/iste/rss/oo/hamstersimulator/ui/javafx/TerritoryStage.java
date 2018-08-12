package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

class TerritoryStage extends Stage {

    private GameSceneController sceneController;

    public TerritoryStage(final Territory territory) throws IOException {
        super();
        prepareStage();
        sceneController.connectToTerritory(territory);
    }

    public void prepareStage() throws IOException {
        this.setTitle("Hamster Simulator - Game Window");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/GameScene.fxml"));
        final Parent root = fxmlLoader.load();
        this.sceneController = (GameSceneController) fxmlLoader.getController();
        final Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add("css/game.css");
        this.setScene(scene);
    }
}
