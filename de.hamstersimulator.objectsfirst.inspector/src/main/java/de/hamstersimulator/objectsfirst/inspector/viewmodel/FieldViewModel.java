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
    private boolean changedByGui = true;

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

    public boolean isChangedByGui() {
        return this.changedByGui;
    }

    public void reloadValue() {
        final Object val = this.reloadValueCallback.get();
        if (val != this.value.getValue()) {
            this.changedByGui = false;
            this.value.set(val);
            this.changedByGui = true;
        }
    }
}
