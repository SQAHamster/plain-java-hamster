package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class ResultDialog extends Dialog<Void> {

    public ResultDialog(final Object value, final InspectionViewModel inspectionViewModel) {
        final DialogPane dialogPane = this.getDialogPane();
        dialogPane.setMaxWidth(300);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        final InputControl inputControl = new InputControl(
                new Type(value != null ? value.getClass() : Object.class),
                inspectionViewModel);
        inputControl.valueProperty().set(value);
        inputControl.isReadOnlyProperty().set(true);
        dialogPane.setContent(inputControl);

        this.setResultConverter(dialogButton -> null);

        this.setTitle("Result");
    }

}