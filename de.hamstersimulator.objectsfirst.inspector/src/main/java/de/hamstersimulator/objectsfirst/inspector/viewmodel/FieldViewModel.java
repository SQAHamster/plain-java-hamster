package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Supplier;

public class FieldViewModel extends ParamViewModel {
    private final SimpleObjectProperty<Object> value;
    private final ReadOnlyBooleanWrapper isFinal;
    final Supplier<Object> reloadValueCallback;

    public FieldViewModel(final String name, final Type type, final Object value, final boolean isFinal, final Supplier<Object> reloadValueCallback) {
        super(name, type);
        this.value = new SimpleObjectProperty<>(this, "value", value);
        this.isFinal = new ReadOnlyBooleanWrapper(this, "isFinal", isFinal);
        this.reloadValueCallback = reloadValueCallback;
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }

    public ReadOnlyBooleanProperty isFinalProperty() {
        return this.isFinal.getReadOnlyProperty();
    }

    public void reloadValue() {
        this.value.set(this.reloadValueCallback.get());
    }
}
