package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.MethodViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.ParamViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CallMethodDialog extends Dialog<List<Object>> {

    public CallMethodDialog(final MethodViewModel method, final InspectionViewModel inspectionViewModel) {
        final DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final GridPane contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPercentWidth(50);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setPercentWidth(50);
        contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        dialogPane.setContent(contentGrid);
        dialogPane.setMaxWidth(300);

        final List<ParamViewModel> params = method.paramsProperty().get();
        final List<InputControl> values = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            final ParamViewModel param = params.get(i);
            final Label nameLabel = new Label();
            nameLabel.textProperty().bind(param.nameProperty());
            contentGrid.add(nameLabel, 0, i);
            final InputControl inputControl = new InputControl(param.typeProperty().get(), inspectionViewModel);
            contentGrid.add(inputControl, 1, i);
            values.add(inputControl);
        }
        final BooleanBinding allValid = Bindings.createBooleanBinding(() ->
                values.stream().allMatch(inputControl -> inputControl.isValidProperty().get()),
                values.stream().map(InputControl::isValidProperty).toArray(BooleanProperty[]::new));
        dialogPane.lookupButton(ButtonType.OK).disableProperty().bind(allValid.not());

        this.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return values.stream().map(InputControl::valueProperty)
                        .map(ObjectProperty::get)
                        .collect(Collectors.toList());
            } else {
                return null;
            }
        });

        this.setTitle("Call Method");
    }

}
