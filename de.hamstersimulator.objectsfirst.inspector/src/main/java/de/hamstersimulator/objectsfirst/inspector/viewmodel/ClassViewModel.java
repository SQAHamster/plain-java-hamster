package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;

public class ClassViewModel extends HideableViewModel {

    private final SimpleListProperty<MethodViewModel> constructors;
    private final SimpleListProperty<MethodViewModel> staticMethods;
    private final SimpleListProperty<FieldViewModel> staticFields;
    private final ReadOnlyObjectWrapper<Class<?>> value;
    private final SimpleBooleanProperty setInstancesAccessible;
    private final SimpleBooleanProperty hasPrivateMembers;

    public ClassViewModel(
            final String name,
            final List<MethodViewModel> constructors,
            final List<MethodViewModel> staticMethods,
            final List<FieldViewModel> staticFields,
            final Class<?> value,
            final boolean setInstancesAccessible,
            final boolean hasPrivateMembers
    ) {
        super(name);
        this.constructors = new SimpleListProperty<>(this, "constructors", FXCollections.observableList(constructors));
        this.staticMethods = new SimpleListProperty<>(this, "staticMethods", FXCollections.observableList(staticMethods));
        this.staticFields = new SimpleListProperty<>(this, "staticFields", FXCollections.observableList(staticFields));
        this.value = new ReadOnlyObjectWrapper<>(this, "value", value);
        this.setInstancesAccessible = new SimpleBooleanProperty(this, "setInstancesAccessible", setInstancesAccessible);
        this.hasPrivateMembers = new SimpleBooleanProperty(this, "hasPrivateMembers", hasPrivateMembers);
    }

    public ListProperty<MethodViewModel> constructorsProperty() {
        return this.constructors;
    }

    public ListProperty<MethodViewModel> staticMethodsProperty() {
        return this.staticMethods;
    }

    public ListProperty<FieldViewModel> staticFieldsProperty() {
        return this.staticFields;
    }

    public BooleanProperty setInstancesAccessibleProperty() {
        return this.setInstancesAccessible;
    }

    public BooleanProperty hasPrivateMembersProperty() {
        return this.hasPrivateMembers;
    }

    public ReadOnlyObjectProperty<Class<?>> valueProperty() {
        return this.value.getReadOnlyProperty();
    }
}
