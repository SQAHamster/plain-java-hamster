package de.hamstersimulator.objectsfirst.ui.javafx;

import java.util.Optional;
import java.util.function.Function;

import de.hamstersimulator.objectsfirst.adapter.InputInterface;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class JavaFXInputInterface implements InputInterface {

    private volatile Optional<Dialog<?>> currentDialog = Optional.empty();

    private class TextDialogWrapper {

        Optional<String> result;

        public void showAndWait(final String message, final String defaultValue, final Function<String, Boolean> validator) {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                final TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
                currentDialog = Optional.of(textInputDialog);
                textInputDialog.setTitle("Hamster needs input!");
                textInputDialog.setHeaderText(message);

                final Button okButton = (Button) textInputDialog.getDialogPane().lookupButton(ButtonType.OK);
                textInputDialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
                final TextField inputField = textInputDialog.getEditor();
                final BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> !validator.apply(inputField.getText()), inputField.textProperty());
                okButton.disableProperty().bind(isInvalid);
                result = textInputDialog.showAndWait();
                currentDialog = Optional.empty();
            });
        }

    }

    @Override
    public Optional<Integer> readInteger(final String message) {
        final TextDialogWrapper wrapper = new TextDialogWrapper();
        wrapper.showAndWait(message, "0", this::validateInt);
        if (wrapper.result.isPresent()) {
            return Optional.of(Integer.valueOf(wrapper.result.orElseThrow(RuntimeException::new)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> readString(final String message) {
        final TextDialogWrapper wrapper = new TextDialogWrapper();
        wrapper.showAndWait(message, "", this::validateString);
        if (wrapper.result.isPresent()) {
            return Optional.of(wrapper.result.orElseThrow(RuntimeException::new));
        } else {
            return Optional.empty();
        }
    }

    private boolean validateString(final String s) {
        return s != null && !s.equals("");
    }

    private boolean validateInt(final String s) {
        try {
            final int result = Integer.valueOf(s);
            return result >= 0;
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    public void confirmAlert(final Throwable t) {
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            final Dialog<ButtonType> alertDialog = new Alert(AlertType.ERROR);
            this.currentDialog = Optional.of(alertDialog);
            alertDialog.setTitle("An exception occurred, program execution stopped.");
            alertDialog.setHeaderText("An exception of type " + t.getClass().getSimpleName() +
                    " occurred.\n" + t.getMessage() + ".\nProgram execution will be aborted. Please "+
                    "fix your program and try again.");
            alertDialog.showAndWait();
            this.currentDialog = Optional.empty();
        });
    }

    @Override
    public void abort() {
        this.currentDialog.ifPresent(dialog -> Platform.runLater(dialog::close));
        this.currentDialog = Optional.empty();
    }

}
