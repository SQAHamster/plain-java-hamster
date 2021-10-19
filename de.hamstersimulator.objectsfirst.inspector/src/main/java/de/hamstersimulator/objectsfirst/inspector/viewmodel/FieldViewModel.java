package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import javafx.beans.property.*;
import javafx.event.EventHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldViewModel extends ParamViewModel {
    private final SimpleObjectProperty<Object> value;
    private final ReadOnlyBooleanWrapper isFinal;

    public FieldViewModel(final String name, final Type type, final Object value, final boolean isFinal) {
        super(name, type);
        this.value = new SimpleObjectProperty<>(this, "value", value);
        this.isFinal = new ReadOnlyBooleanWrapper(this, "isFinal", isFinal);
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }

    public ReadOnlyBooleanProperty isFinalProperty() {
        return this.isFinal.getReadOnlyProperty();
    }
}
