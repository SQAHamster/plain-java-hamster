package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.model.TypeCategory;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.MethodViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ParamViewModel;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUtil;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Control to display a list of methods
 */
public class MethodsTitledPane extends TitledPane {

    private final SimpleListProperty<MethodViewModel> methods;
    private final InspectionViewModel inspectionViewModel;
    private final GridPane contentGrid;

    /**
     * Creates a MethodsTitledPane to display methods of a class or instance
     *
     * @param inspectionViewModel the InspectionViewModel
     */
    public MethodsTitledPane(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.methods = new SimpleListProperty<>(this, "methods");
        this.contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPrefWidth(1000);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setHalignment(HPos.RIGHT);
        secondColumnConstraints.setMinWidth(50);
        this.contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        this.setContent(this.contentGrid);
        this.methods.addListener((ListChangeListener<MethodViewModel>) change -> this.updateLayout(this.methods));
        this.disableProperty().bind(this.methods.emptyProperty());
    }

    /**
     * Updates the layout
     * Must be called when the list of methods changes
     *
     * @param methods the new list of methods
     */
    private void updateLayout(final List<MethodViewModel> methods) {
        this.contentGrid.getChildren().clear();
        for (int i = 0; i < methods.size(); i++) {
            final MethodViewModel method = methods.get(i);
            final Label nameLabel = new Label();
            nameLabel.textProperty().bind(method.methodSignatureProperty());
            this.contentGrid.add(nameLabel, 0, i);
            final Tooltip signatureTooltip = new Tooltip();
            signatureTooltip.textProperty().bind(method.methodSignatureProperty());
            nameLabel.setTooltip(signatureTooltip);
            final Button callButton = createCallButton(method);
            nameLabel.setLabelFor(callButton);
            this.contentGrid.add(callButton, 1, i);
        }
        if (methods.isEmpty()) {
            this.setExpanded(false);
        }
    }

    /**
     * Creates the call button for a specific method
     *
     * @param method viewModel representing the method which will be called on button click
     * @return the generated button
     */
    private Button createCallButton(final MethodViewModel method) {
        final Button callButton = new Button("Call");
        callButton.disableProperty().bind(this.inspectionViewModel.isReadOnly());
        callButton.setOnMouseClicked(e -> {
            final boolean needsInput = !method.paramsProperty().get().isEmpty();
            final Optional<List<Object>> values = needsInput
                    ? new CallMethodDialog(method, this.inspectionViewModel).showAndWait()
                    : Optional.of(Collections.emptyList());
            values.ifPresent(objects -> this.inspectionViewModel.executeOnMainThread(() -> {
                final Object result = method.call(objects);
                if (method.returnTypeProperty().get().getCategory() != TypeCategory.VOID) {
                    JavaFXUtil.blockingExecuteOnFXThread(() -> {
                        new ResultDialog(result, this.inspectionViewModel).showAndWait();
                    });
                }
            }));
        });
        return callButton;
    }

    /**
     * Property for the methods list
     * @return the property representing methods
     */
    public ListProperty<MethodViewModel> methodsProperty() {
        return this.methods;
    }

}
