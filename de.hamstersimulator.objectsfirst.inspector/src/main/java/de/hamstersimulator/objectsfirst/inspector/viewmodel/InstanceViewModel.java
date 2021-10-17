package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.Objects;

public class InstanceViewModel extends NamedViewModel {

    private final ReadOnlyObjectWrapper<ClassViewModel> classInfo;
    private final SimpleListProperty<MethodViewModel> methods;
    private final SimpleListProperty<MethodViewModel> superclassMethods;
    private final SimpleListProperty<FieldViewModel> fields;
    private final SimpleListProperty<FieldViewModel> superclassFields;
    private final ReadOnlyObjectWrapper<?> value;
    private final SimpleBooleanProperty hasPrivateMembers;

    public InstanceViewModel(
            final String name,
            final ClassViewModel classInfo,
            final List<MethodViewModel> methods,
            final List<MethodViewModel> superclassMethods,
            final List<FieldViewModel> fields,
            final List<FieldViewModel> superclassFields,
            final Object value,
            final boolean hasPrivateMembers
    ) {
        super(name);
        this.classInfo = new ReadOnlyObjectWrapper<>(this, "classInfo", classInfo);
        this.methods = new SimpleListProperty<>(this, "methods", FXCollections.observableList(methods));
        this.superclassMethods = new SimpleListProperty<>(this, "superclassMethods", FXCollections.observableList(superclassMethods));
        this.fields = new SimpleListProperty<>(this, "fields", FXCollections.observableList(fields));
        this.superclassFields = new SimpleListProperty<>(this, "superclassFields", FXCollections.observableList(superclassFields));
        this.value = new ReadOnlyObjectWrapper<>(this, "value", value);
        this.hasPrivateMembers = new SimpleBooleanProperty(this, "hasPrivateMembers", hasPrivateMembers);
    }

    public ReadOnlyObjectProperty<ClassViewModel> classInfoProperty() {
        return this.classInfo.getReadOnlyProperty();
    }

    public SimpleListProperty<MethodViewModel> methodsProperty() {
        return this.methods;
    }

    public SimpleListProperty<MethodViewModel> superclassMethodsProperty() {
        return this.superclassMethods;
    }

    public SimpleListProperty<FieldViewModel> fieldsProperty() {
        return this.fields;
    }

    public SimpleListProperty<FieldViewModel> superclassFieldsProperty() {
        return this.superclassFields;
    }

    public ReadOnlyObjectProperty<?> valueProperty() {
        return this.value;
    }

    public BooleanProperty hasPrivateMembersProperty() {
        return this.hasPrivateMembers;
    }

    @Override
    public String toString() {
        return Objects.toString(this.value.get());
    }
}
