package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

/**
 * Top level inspection control
 * Displays a list of instances and a list of classes
 * Items are automatically updated via the provided InstancesListView
 */
public class InspectControl extends SplitPane {
    private final InspectionViewModel inspectionViewModel;
    private InstancesListView instancesListView;
    private ClassesListView classesListView;

    /**
     * Creates a new InspectControl
     * Binds the instances and classes list to the corresponding lists of the inspectionViewModel
     *
     * @param inspectionViewModel used to retrieve the instances and classes list
     */
    public InspectControl(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.getItems().add(this.createClassesArea());
        this.getItems().add(this.createInstancesArea());

        this.setOrientation(Orientation.VERTICAL);
    }

    /**
     * Creates the instances area.
     * Adds the necessary bindings for the instances list
     *
     * @return the created wrapper for the instances area
     */
    private Node createInstancesArea() {
        final VBox outer = new VBox();
        outer.getChildren().add(new Label("Instances"));
        this.instancesListView = new InstancesListView(this.inspectionViewModel);
        this.instancesListView.itemsProperty().bind(this.inspectionViewModel.instancesProperty());
        outer.getChildren().add(createWrappingScrollPane(this.instancesListView));
        return outer;
    }

    /**
     * Creates the classes area.
     * Adds the necessary bindings for the classes list
     *
     * @return the created wrapper for the classes area
     */
    private Node createClassesArea() {
        final VBox outer = new VBox();
        outer.getChildren().add(new Label("Classes"));
        this.classesListView = new ClassesListView(this.inspectionViewModel);
        this.classesListView.itemsProperty().bind(this.inspectionViewModel.classesProperty());
        outer.getChildren().add(createWrappingScrollPane(this.classesListView));
        return outer;
    }

    /**
     * Creates a wrapping ScrollPane for the classes or instances list view
     *
     * @param listView the list view to wrap in a ScrollPane
     * @return the generated ScrollPane
     */
    private Node createWrappingScrollPane(final CardListView<?> listView) {
        final ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFocusTraversable(false);
        scrollPane.setFitToWidth(true);
        scrollPane.prefHeightProperty().bind(this.prefHeightProperty());
        return scrollPane;
    }

    /**
     * Called on close, calls onClose on both the classes and instances listView
     * used to hide popOvers
     */
    public void onClose() {
        this.classesListView.onClose();
        this.instancesListView.onClose();
    }

}
