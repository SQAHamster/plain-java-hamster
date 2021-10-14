package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ClassViewModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ClassesListView extends CardListView<ClassViewModel> {

    private final ClassDetailControl classDetailControl;

    public ClassesListView(final InspectionViewModel inspectionViewModel) {
        this.classDetailControl = new ClassDetailControl(inspectionViewModel);
    }

    @Override
    protected Node createCardContent(ClassViewModel item) {
        final Label label = new Label("test");
        label.textProperty().bind(item.nameProperty());
        return label;
    }

    @Override
    protected Region createPopOverContent(ClassViewModel item) {
        this.classDetailControl.classProperty().set(item);
        return classDetailControl;
    }
}
