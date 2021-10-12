package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import java.util.List;

public class ClassViewModel extends HideableViewModel {

    private final ReadOnlyListWrapper<MethodViewModel> constructors;
    private final ReadOnlyListWrapper<MethodViewModel> staticMethods;
    private final ReadOnlyListWrapper<FieldViewModel> staticFields;
    private final ReadOnlyObjectWrapper<Class<?>> value;

    public ClassViewModel(final String name, final List<MethodViewModel> constructors, final List<MethodViewModel> staticMethods, List<FieldViewModel> staticFields, final Class<?> value) {
        super(name);
        this.constructors = new ReadOnlyListWrapper<>(this, "constructors", FXCollections.observableList(constructors));
        this.staticMethods = new ReadOnlyListWrapper<>(this, "staticMethods", FXCollections.observableList(staticMethods));
        this.staticFields = new ReadOnlyListWrapper<>(this, "staticFields", FXCollections.observableList(staticFields));
        this.value = new ReadOnlyObjectWrapper<>(this, "value", value);
    }

    public ReadOnlyListProperty<MethodViewModel> constructorsProperty() {
        return this.constructors.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<MethodViewModel> staticMethodsProperty() {
        return this.staticMethods.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<FieldViewModel> staticFieldsProperty() {
        return this.staticFields.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Class<?>> valueProperty() {
        return this.value.getReadOnlyProperty();
    }
}