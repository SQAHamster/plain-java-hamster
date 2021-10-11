package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.MethodViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.ParamViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MethodsTitledPane extends TitledPane {

    private final SimpleListProperty<MethodViewModel> methods;
    private final InspectionViewModel inspectionViewModel;
    private final GridPane contentGrid;

    public MethodsTitledPane(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.methods = new SimpleListProperty<>(this, "methods");
        this.contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints(50);
        secondColumnConstraints.setHalignment(HPos.RIGHT);
        this.contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        this.setContent(this.contentGrid);
        this.methods.addListener((ListChangeListener<MethodViewModel>) change -> this.updateLayout(this.methods));
        this.disableProperty().bind(this.methods.emptyProperty());
    }

    private void updateLayout(final List<MethodViewModel> methods) {
        this.contentGrid.getChildren().clear();
        for (int i = 0; i < methods.size(); i++) {
            final MethodViewModel method = methods.get(i);
            final Label nameLabel = new Label();
            nameLabel.textProperty().bind(method.nameProperty());
            this.contentGrid.add(nameLabel, 0, i);
            final Button callButton = new Button("Call");
            callButton.setOnMouseClicked(e -> {
                final CallMethodDialog dialog = new CallMethodDialog(method, this.inspectionViewModel);
                dialog.setWidth(400);
                final boolean needsInput = !method.paramsProperty().get().isEmpty();
                final Optional<List<Object>> values = needsInput
                        ? new CallMethodDialog(method, this.inspectionViewModel).showAndWait()
                        : Optional.of(Collections.emptyList());
                if (values.isPresent()) {
                    final Object result = method.call(values.get());
                    new ResultDialog(result, this.inspectionViewModel).showAndWait();
                }
            });
            this.contentGrid.add(callButton, 1, i);
        }
        if (methods.isEmpty()) {
            this.setExpanded(false);
        }
    }

    public ListProperty<MethodViewModel> methodsProperty() {
        return this.methods;
    }

}
