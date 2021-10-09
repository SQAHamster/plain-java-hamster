package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;

public class ConstructorViewModel<T> extends NamedViewModel {

    private final ReadOnlyListWrapper<ParamViewModel> params;
    private final ReadOnlyObjectWrapper<Class<?>> returnType;
    private final Function<List<?>, T> constructorCallback;

    public ConstructorViewModel(final String name, final List<ParamViewModel> params, final Class<?> returnType, final Function<List<?>, T> constructorCallback) {
        super(name);
        this.params = new ReadOnlyListWrapper<>(this, "params", FXCollections.observableList(params));
        this.returnType = new ReadOnlyObjectWrapper<>(this, "returnType", returnType);
        this.constructorCallback = constructorCallback;
    }

    public ReadOnlyListProperty<ParamViewModel> paramsProperty() {
        return this.params.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Class<?>> returnTypeProperty() {
        return this.returnType.getReadOnlyProperty();
    }

    public T construct(final List<Object> values) {
        return this.constructorCallback.apply(values);
    }
}
