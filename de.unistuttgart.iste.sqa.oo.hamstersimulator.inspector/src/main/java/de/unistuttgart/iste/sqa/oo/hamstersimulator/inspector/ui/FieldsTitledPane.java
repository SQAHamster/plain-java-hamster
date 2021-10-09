package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.FieldViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.List;

public class FieldsTitledPane extends TitledPane {

    private final SimpleListProperty<FieldViewModel> fields;
    private final InspectionViewModel inspectionViewModel;
    private final GridPane contentGrid;

    public FieldsTitledPane(final InspectionViewModel inspectionViewModel) {
        this.inspectionViewModel = inspectionViewModel;
        this.fields = new SimpleListProperty<>(this, "fields");
        this.contentGrid = new GridPane();
        final ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPercentWidth(50);
        final ColumnConstraints secondColumnConstraints = new ColumnConstraints();
        secondColumnConstraints.setPercentWidth(50);
        this.contentGrid.getColumnConstraints().addAll(firstColumnConstraints, secondColumnConstraints);
        this.setContent(this.contentGrid);
        this.fields.addListener((ListChangeListener<FieldViewModel>) change -> this.updateLayout(this.fields));
    }

    private void updateLayout(final List<FieldViewModel> fields) {
        this.contentGrid.getChildren().clear();
        for (int i = 0; i < fields.size(); i++) {
            final FieldViewModel field = fields.get(i);
            final Label nameLabel = new Label();
            nameLabel.textProperty().bind(field.nameProperty());
            this.contentGrid.add(nameLabel, 0, i);
            final InputControl inputControl = new InputControl(field.typeProperty().get(), this.inspectionViewModel);
            inputControl.valueProperty().bindBidirectional(field.valueProperty());
            this.contentGrid.add(inputControl, 1, i);
        }
        if (fields.isEmpty()) {
            this.setExpanded(false);
        }
    }

    public ListProperty<FieldViewModel> fieldsProperty() {
        return this.fields;
    }

}
