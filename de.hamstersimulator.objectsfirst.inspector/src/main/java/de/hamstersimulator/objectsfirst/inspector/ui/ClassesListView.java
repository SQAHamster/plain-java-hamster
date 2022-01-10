package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ClassViewModel;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * ListView for a list of Classes
 */
public class ClassesListView extends CardListView<ClassViewModel> {

    /**
     * Detail control for the popOver content
     */
    private final ClassDetailControl classDetailControl;

    /**
     * Creates a new ClassesListView
     *
     * @param inspectionViewModel used to create the detail control for the selected class
     */
    public ClassesListView(final InspectionViewModel inspectionViewModel) {
        this.classDetailControl = new ClassDetailControl(inspectionViewModel);
    }

    @Override
    protected ObservableStringValue getCardText(ClassViewModel item) {
        return item.nameProperty();
    }

    @Override
    protected Region createPopOverContent(ClassViewModel item) {
        this.classDetailControl.classProperty().set(item);
        return classDetailControl;
    }
}
