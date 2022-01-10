package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InstanceViewModel;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * ListView for a list of instances
 */
public class InstancesListView extends CardListView<InstanceViewModel> {

    /**
     * Detail control for the popOver content
     */
    private final InstanceDetailControl instanceDetailControl;

    /**
     * Creates a new ClassesListView
     *
     * @param inspectionViewModel used to create the detail control for the selected instance
     */
    public InstancesListView(final InspectionViewModel inspectionViewModel) {
        this.instanceDetailControl = new InstanceDetailControl(inspectionViewModel);
        this.instanceDetailControl.setMinWidth(300);
    }

    @Override
    protected ObservableStringValue getCardText(InstanceViewModel item) {
        return item.nameProperty();
    }

    @Override
    protected Region createPopOverContent(InstanceViewModel item) {
        this.instanceDetailControl.instanceProperty().set(item);
        return instanceDetailControl;
    }
}
