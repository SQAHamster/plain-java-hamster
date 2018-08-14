package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

class HamsterGameStage extends Stage {

    private GameSceneController sceneController;

    public HamsterGameStage(final Territory territory) throws IOException {
        super();
        prepareStage();
        sceneController.connectToTerritory(territory);
    }

    public void prepareStage() throws IOException {
        this.setTitle("Hamster Simulator - Game Window");
        final Scene scene = new Scene(loadFromFXML(), 300, 250);
        scene.getStylesheets().add("css/game.css");
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
