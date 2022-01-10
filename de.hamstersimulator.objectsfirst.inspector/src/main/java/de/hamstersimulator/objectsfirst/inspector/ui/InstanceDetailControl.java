package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InstanceViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

/**
 * Detail control to display fields and methods of an instance
 * Typically shown in a popOver
 */
public class InstanceDetailControl extends VBox {

    private final SimpleObjectProperty<InstanceViewModel> instance;

    /**
     * Creates a new InstanceDetailControl
     *
     * @param inspectionViewModel used to generate methods and field panes
     */
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

    /**
     * Property for the current instance
     * @return the property for instance
     */
    public ObjectProperty<InstanceViewModel> instanceProperty(){
        return this.instance;
    }
}
