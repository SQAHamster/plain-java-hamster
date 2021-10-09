package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FieldViewModel extends ParamViewModel {
    private SimpleObjectProperty<Object> value;

    public FieldViewModel(final String name, final Type type, final Object value) {
        super(name, type);
        this.value = new SimpleObjectProperty<>(this, "value", value);
    }

    public ObjectProperty<Object> valueProperty() {
        return this.value;
    }
}
