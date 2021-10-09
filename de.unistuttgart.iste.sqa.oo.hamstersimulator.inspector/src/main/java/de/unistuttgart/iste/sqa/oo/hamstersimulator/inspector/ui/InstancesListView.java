package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InstanceViewModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class InstancesListView extends CardListView<InstanceViewModel<?>> {

    private final InstanceDetailControl instanceDetailControl;

    public InstancesListView(final InspectionViewModel inspectionViewModel) {
        this.instanceDetailControl = new InstanceDetailControl(inspectionViewModel);
        this.instanceDetailControl.setMinWidth(300);
    }

    @Override
    protected Node createCardContent(InstanceViewModel<?> item) {
        final Label label = new Label();
        label.textProperty().bind(item.nameProperty());
        label.setMaxWidth(100);
        return label;
    }

    @Override
    protected Region createPopOverContent(InstanceViewModel<?> item) {
        this.instanceDetailControl.instanceProperty().set(item);
        return instanceDetailControl;
    }
}
