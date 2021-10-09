package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InstanceViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Accordion;
import javafx.scene.layout.VBox;

public class InstanceDetailControl extends VBox {

    private final SimpleObjectProperty<InstanceViewModel<?>> instance;

    public InstanceDetailControl(final InspectionViewModel inspectionViewModel) {
        this.instance = new SimpleObjectProperty<>(this, "instance");

        final Accordion accordion = new Accordion();
        final FieldsTitledPane fieldsPane = new FieldsTitledPane(inspectionViewModel);
        fieldsPane.setText("Fields");
        accordion.getPanes().addAll(fieldsPane);
        this.getChildren().add(accordion);

        this.instance.addListener((observable, oldValue, newValue) -> {
            fieldsPane.fieldsProperty().unbind();
            if (newValue != null) {
                fieldsPane.fieldsProperty().bind(newValue.fieldsProperty());
            }
        });
    }

    public ObjectProperty<InstanceViewModel<?>> instanceProperty(){
        return this.instance;
    }
}
