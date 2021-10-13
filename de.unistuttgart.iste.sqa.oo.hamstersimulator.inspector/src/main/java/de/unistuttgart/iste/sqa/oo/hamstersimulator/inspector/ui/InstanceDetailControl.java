package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InstanceViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

public class InstanceDetailControl extends VBox {

    private final SimpleObjectProperty<InstanceViewModel> instance;

    public InstanceDetailControl(final InspectionViewModel inspectionViewModel) {
        this.instance = new SimpleObjectProperty<>(this, "instance");

        final FieldsTitledPane fieldsPane = new FieldsTitledPane(inspectionViewModel);
        fieldsPane.setText("Fields");

        final MethodsTitledPane methodsPane = new MethodsTitledPane(inspectionViewModel);
        methodsPane.setText("Methods");

        final FieldsTitledPane superClassFieldsPane = new FieldsTitledPane(inspectionViewModel);
        superClassFieldsPane.setText("Superclass Fields");

        final MethodsTitledPane superclassMethodsPane = new MethodsTitledPane(inspectionViewModel);
        superclassMethodsPane.setText("Superclass Methods");

        this.getChildren().addAll(fieldsPane, methodsPane, superClassFieldsPane, superclassMethodsPane);

        this.instance.addListener((observable, oldValue, newValue) -> {
            fieldsPane.fieldsProperty().unbind();
            methodsPane.methodsProperty().unbind();
            superClassFieldsPane.fieldsProperty().unbind();
            superclassMethodsPane.methodsProperty().unbind();
            if (newValue != null) {
                fieldsPane.fieldsProperty().bind(newValue.fieldsProperty());
                methodsPane.methodsProperty().bind(newValue.methodsProperty());
                superClassFieldsPane.fieldsProperty().bind(newValue.superclassFieldsProperty());
                superclassMethodsPane.methodsProperty().bind(newValue.superclassMethodsProperty());
            }
        });
    }

    public ObjectProperty<InstanceViewModel> instanceProperty(){
        return this.instance;
    }
}
