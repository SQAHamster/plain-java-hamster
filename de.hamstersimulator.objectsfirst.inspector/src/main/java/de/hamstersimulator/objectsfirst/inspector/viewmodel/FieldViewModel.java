package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldViewModel extends ParamViewModel {
    private final SimpleObjectProperty<Object> value;

    public FieldViewModel(final String name, final Type type, final Object value) {
        super(name, type);
        this.value = new SimpleObjectProperty<>(this, "value", value);
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }
}
