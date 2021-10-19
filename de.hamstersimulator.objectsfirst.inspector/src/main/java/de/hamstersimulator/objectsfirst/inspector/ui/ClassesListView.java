package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ClassViewModel;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ClassesListView extends CardListView<ClassViewModel> {

    private final ClassDetailControl classDetailControl;

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
