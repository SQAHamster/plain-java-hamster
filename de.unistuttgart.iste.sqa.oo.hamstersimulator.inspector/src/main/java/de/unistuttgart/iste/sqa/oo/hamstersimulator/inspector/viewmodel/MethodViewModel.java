package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.function.Function;

public class MethodViewModel extends NamedViewModel {

    private final ReadOnlyListWrapper<ParamViewModel> params;
    private final ReadOnlyObjectWrapper<Class<?>> returnType;
    private final Function<List<?>, ?> methodCallback;

    public MethodViewModel(final String name, final List<ParamViewModel> params, final Class<?> returnType, final Function<List<?>, ?> methodCallback) {
        super(name);
        this.params = new ReadOnlyListWrapper<>(this, "params", FXCollections.observableList(params));
        this.returnType = new ReadOnlyObjectWrapper<>(this, "returnType", returnType);
        this.methodCallback = methodCallback;
    }

    public ReadOnlyListProperty<ParamViewModel> paramsProperty() {
        return this.params.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Class<?>> returnTypeProperty() {
        return this.returnType.getReadOnlyProperty();
    }

    public Object call(final List<Object> values) {
        return this.methodCallback.apply(values);
    }
}
