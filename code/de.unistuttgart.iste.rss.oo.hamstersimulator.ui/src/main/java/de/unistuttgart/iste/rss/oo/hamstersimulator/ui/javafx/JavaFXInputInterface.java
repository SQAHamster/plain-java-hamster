package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.Optional;
import java.util.function.Function;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InputInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class JavaFXInputInterface implements InputInterface {

    private class TextDialogWrapper {

        Optional<String> result;
        
        public void showAndWait(final String message, final String defaultValue, final Function<String, Boolean> validator) {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                final TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
                textInputDialog.setTitle("Hamster needs input!");
                textInputDialog.setHeaderText(message);
                
                final Button okButton = (Button) textInputDialog.getDialogPane().lookupButton(ButtonType.OK);
                textInputDialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
                final TextField inputField = textInputDialog.getEditor();
                final BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> !validator.apply(inputField.getText()), inputField.textProperty());
                okButton.disableProperty().bind(isInvalid);
                
                result = textInputDialog.showAndWait();
            });
        }
        
    }

    @Override
    public int readInteger() {
        final TextDialogWrapper wrapper = new TextDialogWrapper();
        wrapper.showAndWait("Please provide a positive number:", "0", this::validateInt);
        final int intResult = Integer.valueOf(wrapper.result.orElseThrow(RuntimeException::new));
        return intResult;
    }
    
    @Override
    public String readString() {
        final TextDialogWrapper wrapper = new TextDialogWrapper();
        wrapper.showAndWait("Please provide a string:", "", this::validateString);
        return wrapper.result.orElseThrow(RuntimeException::new);
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

}
