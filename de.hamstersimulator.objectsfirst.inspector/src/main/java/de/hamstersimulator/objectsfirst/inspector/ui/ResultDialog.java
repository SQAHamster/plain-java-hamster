package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Dialog to show the result of a function call
 * Displays a InputControl which can be used to copy the (primitive) result value or
 * save the instance to the instances list
 */
public class ResultDialog extends Dialog<Void> {

    /**
     * Creates a new ResultDialog. Does not show the dialog automatically
     *
     * @param value the result value
     * @param inspectionViewModel necessary InspectionViewModel for generated InputControl
     */
    public ResultDialog(final Object value, final InspectionViewModel inspectionViewModel) {
        final Type returnValType = new Type(value != null ? value.getClass() : Object.class);
        final DialogPane dialogPane = this.getDialogPane();
        final GridPane contentGrid = createContentGrid();
        dialogPane.setContent(contentGrid);
        dialogPane.setMaxWidth(300);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        Label valueText = new Label();
        valueText.textProperty().set("Returned value:");
        contentGrid.add(valueText, 0, 1);

        final InputControl inputControl = generateInputControl(returnValType, inspectionViewModel, value);
        contentGrid.add(inputControl, 1, 1);
        createTypeTextLabels(value, returnValType, contentGrid);

        this.setResultConverter(dialogButton -> null);
        this.setTitle("Result");
    }

    /**
     * Creates the contentGrid.
     * Also sets the size constraints for the columns of the grid
     *
     * @return the created content grid
     */
    private GridPane createContentGrid() {
        final GridPane contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPercentWidth(50);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setPercentWidth(50);
        contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        return contentGrid;
    }

    /**
     * Creates the label to display the result type and adds them to the contentGrid
     *
     * @param value the result value
     * @param returnValType the type of the return value
     * @param contentGrid the grid to add the generated labels to
     */
    private void createTypeTextLabels(final Object value, final Type returnValType, final GridPane contentGrid) {
        if (value != null) {
            Label typeText = new Label();
            typeText.textProperty().set("Returned type:");
            contentGrid.add(typeText, 0, 0);
            Label typeLabel = new Label();
            typeLabel.textProperty().set(returnValType.getType().getSimpleName());
            contentGrid.add(typeLabel, 1, 0);
        }
    }

    /***
     * Creates the InputControl used to possibly save the result instance or copy the value
     *
     * @param returnValType the type of for the InputControl
     * @param inspectionViewModel necessary InspectionViewModel for instances list
     * @param value the value displayed in the InputControl
     * @return the generated InputControl
     */
    private InputControl generateInputControl(final Type returnValType, final InspectionViewModel inspectionViewModel,
                                              final Object value) {
        final InputControl inputControl = new InputControl(
                returnValType,
                inspectionViewModel);
        inputControl.valueProperty().set(value);
        inputControl.isReadOnlyProperty().set(true);
        return inputControl;
    }

}
