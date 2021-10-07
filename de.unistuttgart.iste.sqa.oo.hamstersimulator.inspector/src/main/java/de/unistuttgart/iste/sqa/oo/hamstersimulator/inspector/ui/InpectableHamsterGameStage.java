package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx.HamsterGameStage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InpectableHamsterGameStage extends HamsterGameStage {
    public InpectableHamsterGameStage(HamsterGameViewModel hamsterGameViewModel, InspectionViewModel inspect) throws IOException {
        super(hamsterGameViewModel);
        Thread showing = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Classes:");
                    inspect.getTypes().forEach(System.out::println);
                    System.out.println("Variables:");
                    inspect.getVariables().forEach((key, val) -> System.out.println(key + ": " + val.toString()));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {

            }
        });
        showing.setDaemon(true);
        showing.start();
        /*VBox split = new VBox();
        split.getChildren().add(createLayout());
        split.getChildren().add(this.getScene().getRoot());
        Scene newScene = new Scene(split, 1280, 720);
        this.setScene(newScene);*/
    }
}
