package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Instance;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
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
import org.controlsfx.control.PopOver;

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
                    inspect.typesProperty().forEach(System.out::println);
                    System.out.println("Variables:");
                    inspect.variablesProperty().forEach((key, val) -> System.out.println(key + ": " + val.toString()));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {

            }
        });
        showing.setDaemon(true);
        showing.start();
        VBox split = new VBox();
        split.getChildren().add(createLayout(inspect));
        split.getChildren().add(this.getScene().getRoot());
        Scene newScene = new Scene(split, 1280, 720);
        this.setScene(newScene);
    }

    private Parent createLayout(InspectionViewModel inspect) {
        final VBox outer = new VBox();
        final FlowPane flowPane = new FlowPane();
        flowPane.setVgap(10);
        flowPane.setHgap(10);
        for (int i = 0; i < 10; i++) {
            final StackPane stackPane = new StackPane();
            final Rectangle rectangle = new Rectangle();
            rectangle.setWidth(200);
            rectangle.setHeight(100);
            rectangle.setArcWidth(10);
            rectangle.setArcHeight(10);
            rectangle.setFill(Color.LIGHTGRAY);
            stackPane.getChildren().add(rectangle);
            final Label label = new Label("Hello Label asdf as df asd fas df asd f asdf as dfasdfasdfhjasfgjhkadsgfkjagsdkjhf asd fa sdfasdfasdf as dfasdfasdfasd fasdfasdfas fa sdf".substring(0, i * 10));
            label.setMaxWidth(150);
            label.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(label);
            flowPane.getChildren().add(stackPane);
            stackPane.setOnMouseClicked(e -> {
                addPopOver(rectangle);
            });
        }

        outer.getChildren().add(flowPane);

        final Type testType = Type.typeForClass(Object.class);
        final InputControl testInputControl = new InputControl(testType, inspect);

        //final FieldInfo testFieldInfo = new FieldInfo("test", Object.class, testInstanceInfo);
        //testInputControl.valueProperty().bindBidirectional(testFieldInfo.valueProperty());
        outer.getChildren().add(testInputControl);

        return outer;
    }

    private void addPopOver(final Node owner) {
        final Label content = new Label("Hello content");
        final PopOver popOver = new PopOver(content);
        popOver.setDetachable(false);
        popOver.show(owner);
    }
}
