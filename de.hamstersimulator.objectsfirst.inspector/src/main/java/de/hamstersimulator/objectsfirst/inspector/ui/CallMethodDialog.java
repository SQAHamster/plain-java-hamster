package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.MethodViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ParamViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog to enter method arguments
 */
public class CallMethodDialog extends Dialog<List<Object>> {

    /**
     * The method which will be called
     */
    private final MethodViewModel method;
    /**
     * Overall ViewModel for the inspection
     */
    private final InspectionViewModel inspectionViewModel;
    /**
     * The GridPane with all InputControls to enter arguments for all parameters
     */
    private final GridPane contentGrid;

    /**
     * Creates a new CallMethodDialog
     * Takes the parameters from the method
     *
     * @param method the method which will be called
     * @param inspectionViewModel the overall ViewModel for the inspection
     */
    public CallMethodDialog(final MethodViewModel method, final InspectionViewModel inspectionViewModel) {
        this.method = method;
        this.inspectionViewModel = inspectionViewModel;

        final DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.contentGrid = this.createChildGridPain();

        this.initTitle();
        this.createParameterInputs();
        dialogPane.getScene().getStylesheets().add("style.css");
    }

    /**
     * Creates a GridPane used to add arguments
     *
     * @return the created GridPane
     */
    private GridPane createChildGridPain() {
        final DialogPane dialogPane = this.getDialogPane();
        final GridPane contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPercentWidth(50);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setPercentWidth(50);
        contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        dialogPane.setContent(contentGrid);
        dialogPane.setMaxWidth(300);
        return contentGrid;
    }

    /**
     * Creates InputControls for all parameters and adds them to the contentGrid
     * Also binds the OK_DONE button to an all valid property, and sets the
     * result converter
     */
    private void createParameterInputs() {
        final List<InputControl> inputControls = this.createInputControls();
        this.bindOKButtonToValidation(inputControls);
        this.createResultConverter(inputControls);
    }

    /**
     * Sets the title of the Dialog and adds a label on top of the control
     * Must be called before createParameterInputs
     */
    private void initTitle() {
        final Label methodNameLabel = new Label();
        final StringProperty titleStart = new SimpleStringProperty("Calling method ");
        methodNameLabel.textProperty().bind(titleStart.concat(method.nameProperty()));
        this.contentGrid.add(methodNameLabel, 0, 0, 2, 1);

        this.setTitle("Call Method");
    }

    /**
     * Creates InputControls for all parameters and adds them to the contentGrid
     *
     * @return the list with all InputControls created
     */
    private List<InputControl> createInputControls() {
        final List<ParamViewModel> params = method.paramsProperty().get();
        final List<InputControl> inputControls = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            final ParamViewModel param = params.get(i);
            final Label nameLabel = new Label();
            final StringExpression methodSignature = param.typeProperty().asString().concat(" ")
                    .concat(param.nameProperty());
            nameLabel.textProperty().bind(methodSignature);
            contentGrid.add(nameLabel, 0, i+1);
            final InputControl inputControl = new InputControl(param.typeProperty().get(), inspectionViewModel);
            contentGrid.add(inputControl, 1, i+1);
            inputControls.add(inputControl);
        }
        return inputControls;
    }

    /**
     * Binds the OK buttons disableProperty to the negated allValid property
     *
     * @param inputControls a list of InputControls which must be all valid
     */
    private void bindOKButtonToValidation(final List<InputControl> inputControls) {
        final DialogPane dialogPane = this.getDialogPane();

        final List<ReadOnlyBooleanProperty> dependentProperties = inputControls.stream()
                .map(InputControl::isValidProperty).toList();

        final BooleanBinding allValid = Bindings.createBooleanBinding(
                () -> dependentProperties.stream().allMatch(ReadOnlyBooleanProperty::get),
                dependentProperties.toArray(ReadOnlyBooleanProperty[]::new));
        dialogPane.lookupButton(ButtonType.OK).disableProperty().bind(allValid.not());
    }

    /**
     * Creates and sets a ResultConverter, which converts the current value of all inputControls
     *
     * @param inputControls a list with all InputControls to enter arguments
     */
    private void createResultConverter(final List<InputControl> inputControls) {
        this.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return inputControls.stream().map(InputControl::valueProperty)
                        .map(ObjectProperty::get)
                        .collect(Collectors.toList());
            } else {
                return null;
            }
        });
    }

}
