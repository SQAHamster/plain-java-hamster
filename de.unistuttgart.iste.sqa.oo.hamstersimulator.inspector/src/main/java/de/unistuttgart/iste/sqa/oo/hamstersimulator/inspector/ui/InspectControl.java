package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class InspectControl extends SplitPane {
    private final InspectionViewModel inspectionViewModel;

    public InspectControl(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.getItems().add(this.createClassesArea());
        this.getItems().add(this.createInstancesArea());

        this.setOrientation(Orientation.VERTICAL);
    }

    private Node createInstancesArea() {
        final VBox outer = new VBox();
        outer.getChildren().add(new Label("Instances"));
        final InstancesListView instancesListView = new InstancesListView(this.inspectionViewModel);
        instancesListView.itemsProperty().bind(this.inspectionViewModel.instancesProperty());
        final ScrollPane instancesScrollPane = new ScrollPane(instancesListView);
        instancesScrollPane.getStyleClass().add("scroll-pane");
        instancesScrollPane.setFocusTraversable(false);
        instancesScrollPane.setFitToWidth(true);
        instancesScrollPane.prefHeightProperty().bind(this.prefHeightProperty());
        outer.getChildren().add(instancesScrollPane);
        return outer;
    }

    private Node createClassesArea() {
        final VBox outer = new VBox();
        outer.getChildren().add(new Label("Classes"));
        final ClassesListView classesListView = new ClassesListView(this.inspectionViewModel);
        classesListView.itemsProperty().bind(this.inspectionViewModel.classesProperty());
        final ScrollPane classesScrollPane = new ScrollPane(classesListView);
        classesScrollPane.getStyleClass().add("scroll-pane");
        classesScrollPane.setFocusTraversable(false);
        classesScrollPane.setFitToWidth(true);
        classesScrollPane.prefHeightProperty().bind(this.prefHeightProperty());
        outer.getChildren().add(classesScrollPane);
        return outer;
    }


}
