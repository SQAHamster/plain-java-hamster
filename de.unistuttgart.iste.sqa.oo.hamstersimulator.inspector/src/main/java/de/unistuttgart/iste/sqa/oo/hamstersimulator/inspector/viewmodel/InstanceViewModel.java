package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InstanceViewModel extends NamedViewModel {

    private final ReadOnlyObjectWrapper<ClassViewModel> classInfo;
    private final ReadOnlyListWrapper<MethodViewModel> methods;
    private final ReadOnlyListWrapper<MethodViewModel> superclassMethods;
    private final ReadOnlyListWrapper<FieldViewModel> fields;
    private final ReadOnlyListWrapper<FieldViewModel> superclassFields;
    private final ReadOnlyObjectWrapper<?> value;

    public InstanceViewModel(
            final String name,
            final ClassViewModel classInfo,
            final List<MethodViewModel> methods,
            final List<MethodViewModel> superclassMethods,
            final List<FieldViewModel> fields,
            final List<FieldViewModel> superclassFields,
            final Object value) {
        super(name);
        this.classInfo = new ReadOnlyObjectWrapper<>(this, "classInfo", classInfo);
        this.methods = new ReadOnlyListWrapper<>(this, "methods", FXCollections.observableList(methods));
        this.superclassMethods = new ReadOnlyListWrapper<>(this, "superclassMethods", FXCollections.observableList(superclassMethods));
        this.fields = new ReadOnlyListWrapper<>(this, "fields", FXCollections.observableList(fields));
        this.superclassFields = new ReadOnlyListWrapper<>(this, "superclassFields", FXCollections.observableList(superclassFields));
        this.value = new ReadOnlyObjectWrapper<>(this, "value", value);
    }

    public ReadOnlyObjectProperty<ClassViewModel> classInfoProperty() {
        return this.classInfo.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<MethodViewModel> methodsProperty() {
        return this.methods.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<MethodViewModel> superclassMethodsProperty() {
        return this.superclassMethods.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<FieldViewModel> fieldsProperty() {
        return this.fields.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<FieldViewModel> superclassFieldsProperty() {
        return this.superclassFields.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<?> valueProperty() {
        return this.value;
    }

    @Override
    public String toString() {
        return Objects.toString(this.value.get());
    }
}
