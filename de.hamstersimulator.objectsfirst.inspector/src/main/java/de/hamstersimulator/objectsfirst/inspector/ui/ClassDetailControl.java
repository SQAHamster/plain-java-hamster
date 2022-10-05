package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ClassViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

/**
 * Detail control to display fields and methods of a class
 * Typically shown in a popOver
 */
public class ClassDetailControl extends VBox {

    private final SimpleObjectProperty<ClassViewModel> cls;

    /**
     * Creates a new ClassDetailControl
     *
     * @param inspectionViewModel used to generate methods and field panes
     */
    public ClassDetailControl(final InspectionViewModel inspectionViewModel) {
        this.cls = new SimpleObjectProperty<>(this, "class");
        final MethodsTitledPane constructorsPane = new MethodsTitledPane(inspectionViewModel);
        constructorsPane.setText("Constructors");
        final FieldsTitledPane staticFieldsPane = new FieldsTitledPane(inspectionViewModel);
        staticFieldsPane.setText("Static Fields");
        final MethodsTitledPane staticMethodsPane = new MethodsTitledPane(inspectionViewModel);
        staticMethodsPane.setText("Static Methods");
        this.getChildren().addAll(constructorsPane, staticFieldsPane, staticMethodsPane);

        this.cls.addListener((observable, oldValue, newValue) -> {
            constructorsPane.methodsProperty().unbind();
            staticFieldsPane.fieldsProperty().unbind();
            staticMethodsPane.methodsProperty().unbind();
            if (newValue != null) {
                constructorsPane.methodsProperty().bind(newValue.constructorsProperty());
                staticFieldsPane.fieldsProperty().bind(newValue.staticFieldsProperty());
                staticMethodsPane.methodsProperty().bind(newValue.staticMethodsProperty());
            }
        });
    }

    /**
     * Property for the current class
     * @return the property for cls
     */
    public ObjectProperty<ClassViewModel> classProperty(){
        return this.cls;
    }
}
