package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ResultDialog extends Dialog<Void> {

    public ResultDialog(final Object value, final InspectionViewModel inspectionViewModel) {
        final Type returnValType = new Type(value != null ? value.getClass() : Object.class);
        final DialogPane dialogPane = this.getDialogPane();
        final GridPane contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPercentWidth(50);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setPercentWidth(50);
        contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        dialogPane.setContent(contentGrid);
        dialogPane.setMaxWidth(300);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        Label valueText = new Label();
        valueText.textProperty().set("Returned value:");
        contentGrid.add(valueText, 0, 1);

        final InputControl inputControl = new InputControl(
                returnValType,
                inspectionViewModel);
        inputControl.valueProperty().set(value);
        inputControl.isReadOnlyProperty().set(true);
        contentGrid.add(inputControl, 1, 1);
        //dialogPane.setContent(inputControl);

        Label typeText = new Label();
        typeText.textProperty().set("Returned type:");
        contentGrid.add(typeText, 0, 0);

        Label typeLabel = new Label();
        typeLabel.textProperty().set(returnValType.toString());
        contentGrid.add(typeLabel,  1, 0);

        this.setResultConverter(dialogButton -> null);

        this.setTitle("Result");
    }

}
