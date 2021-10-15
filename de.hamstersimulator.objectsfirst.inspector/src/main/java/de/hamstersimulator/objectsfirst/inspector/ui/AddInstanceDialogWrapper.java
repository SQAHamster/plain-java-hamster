package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class AddInstanceDialogWrapper {

    public static void showAndWait(final InspectionViewModel inspectionViewModel, final Object value) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter instance name");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        final TextField inputField = dialog.getEditor();
        final BooleanBinding invalidBinding = Bindings.createBooleanBinding(() -> {
            final String text = inputField.getText();
            if (text.isBlank()) {
                return true;
            } else {
                return inspectionViewModel.instancesProperty().stream()
                        .anyMatch(instance -> instance.nameProperty().get().equals(text.trim()));
            }
        }, inputField.textProperty());
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(invalidBinding);
        dialog.showAndWait()
                .map(String::trim)
                .ifPresent(name -> inspectionViewModel.createInstanceViewModel(value, name));
    }
}
