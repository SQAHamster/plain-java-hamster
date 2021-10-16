package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class InspectControl extends SplitPane {
    private final InspectionViewModel inspectionViewModel;
    private InstancesListView instancesListView;
    private ClassesListView classesListView;

    public InspectControl(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.getItems().add(this.createClassesArea());
        this.getItems().add(this.createInstancesArea());

        this.setOrientation(Orientation.VERTICAL);
    }

    private Node createInstancesArea() {
        final VBox outer = new VBox();
        outer.getChildren().add(new Label("Instances"));
        this.instancesListView = new InstancesListView(this.inspectionViewModel);
        this.instancesListView.itemsProperty().bind(this.inspectionViewModel.instancesProperty());
        final ScrollPane instancesScrollPane = new ScrollPane(this.instancesListView);
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
        this.classesListView = new ClassesListView(this.inspectionViewModel);
        this.classesListView.itemsProperty().bind(this.inspectionViewModel.classesProperty());
        final ScrollPane classesScrollPane = new ScrollPane(this.classesListView);
        classesScrollPane.getStyleClass().add("scroll-pane");
        classesScrollPane.setFocusTraversable(false);
        classesScrollPane.setFitToWidth(true);
        classesScrollPane.prefHeightProperty().bind(this.prefHeightProperty());
        outer.getChildren().add(classesScrollPane);
        return outer;
    }

    public void onClose() {
        this.classesListView.onClose();
        this.instancesListView.onClose();
    }

}
