package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.lang.reflect.Parameter;

public class ParamViewModel extends NamedViewModel {
    private final ReadOnlyObjectWrapper<Class<?>> type;

    public ParamViewModel(final String name, final Class<?> type) {
        super(name);
        this.type = new ReadOnlyObjectWrapper<>(this, "type", type);
    }

    public ReadOnlyObjectProperty<Class<?>> typeProperty() {
        return this.type.getReadOnlyProperty();
    }

    public static ParamViewModel fromParameter(final Parameter parameter) {
        return new ParamViewModel(parameter.getName(), parameter.getType());
    }
}
