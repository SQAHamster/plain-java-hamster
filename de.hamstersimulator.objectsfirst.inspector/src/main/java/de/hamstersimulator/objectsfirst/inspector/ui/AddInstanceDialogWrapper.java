package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * Class with static helper method to show dialog to add an object to the instances list
 */
public final class AddInstanceDialogWrapper {

    /**
     * Creates and shows (blocking) a dialog which lets the user enter a name for an instance,
     * and saves the instance under that name to the instances list of the inspectionViewModel.
     * Also validates the entered name
     * If the user approves at the end, the InstanceViewModel is created and added to the list of instances
     *
     * @param inspectionViewModel used to add the created instance
     * @param value the value of the instance to add
     */
    public static void showAndWait(final InspectionViewModel inspectionViewModel, final Object value) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter instance name");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        final TextField inputField = dialog.getEditor();
        final BooleanBinding invalidBinding = createInvalidBinding(inspectionViewModel, inputField);
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(invalidBinding);
        dialog.showAndWait()
                .map(String::trim)
                .ifPresent(name -> inspectionViewModel.createInstanceViewModel(value, name));
    }

    /**
     * Creates a binding if the values of the current dialog are invalid
     * The input is invalid if the text of the input text field is blank, or (trimmed) equal to the
     * name of an already existing instance
     *
     * @param inspectionViewModel the InspectionViewModel containing the list of all instances
     * @param inputField the TextField used for the name input
     * @return a binding which is true, if the entered name is invalid
     */
    private static BooleanBinding createInvalidBinding(InspectionViewModel inspectionViewModel, TextField inputField) {
        return Bindings.createBooleanBinding(() -> {
            final String text = inputField.getText();
            if (text.isBlank()) {
                return true;
            } else {
                return inspectionViewModel.instancesProperty().stream()
                        .anyMatch(instance -> instance.nameProperty().get().equals(text.trim()));
            }
        }, inputField.textProperty());
    }
}
