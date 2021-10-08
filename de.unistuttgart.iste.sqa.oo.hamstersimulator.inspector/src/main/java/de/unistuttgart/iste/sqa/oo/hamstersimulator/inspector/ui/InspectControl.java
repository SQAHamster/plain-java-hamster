package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata.A;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;

public class InspectControl extends SplitPane {
    private final InspectionViewModel inspectionViewModel;

    public InspectControl(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;

        //TODO
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

        final Type testType = new Type(A.class);
        final InputControl testInputControl = new InputControl(testType, this.inspectionViewModel);


        outer.getChildren().add(testInputControl);
        final ScrollPane scrollPane = new ScrollPane(outer);
        scrollPane.setFitToWidth(true);
        this.getItems().add(scrollPane);
    }

    private void addPopOver(final Node owner) {
        final Label content = new Label("Hello content");
        final PopOver popOver = new PopOver(content);
        popOver.setDetachable(false);
        popOver.show(owner);
    }
}
