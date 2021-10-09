package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.ClassViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Accordion;
import javafx.scene.layout.VBox;

public class ClassDetailControl extends VBox {

    private final SimpleObjectProperty<ClassViewModel<?>> cls;

    public ClassDetailControl(final InspectionViewModel inspectionViewModel) {
        this.cls = new SimpleObjectProperty<>(this, "class");

        final Accordion accordion = new Accordion();
        final FieldsTitledPane fieldsPane = new FieldsTitledPane(inspectionViewModel);
        accordion.getPanes().addAll(fieldsPane);
        this.getChildren().add(accordion);

        this.cls.addListener((observable, oldValue, newValue) -> {
            fieldsPane.fieldsProperty().unbind();
            if (newValue != null) {
                fieldsPane.fieldsProperty().bind(newValue.staticFieldsProperty());
            }
        });
    }

    public ObjectProperty<ClassViewModel<?>> classProperty(){
        return this.cls;
    }
}
