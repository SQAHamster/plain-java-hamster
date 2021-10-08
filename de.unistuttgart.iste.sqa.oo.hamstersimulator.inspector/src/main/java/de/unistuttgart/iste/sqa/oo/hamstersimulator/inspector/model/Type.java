package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.ref.WeakReference;
import java.util.*;

public class Type {
    private final Class<?> type;
    private final Type parentType;
    private final Primitives primitiveType;
    private final SimpleListProperty<Type> referencesTypes = new SimpleListProperty<>(this, "referencesTypes", FXCollections.observableList(new ArrayList<>()));
    private final SimpleListProperty<WeakReference<Instance>> knownInstances = new SimpleListProperty<>(this, "knownInstances", FXCollections.observableList(new ArrayList<>()));

    private static final Map<Class<?>, Type> knownTypes = new HashMap<>();

    protected Type(Class<?> type, Type parent) {
        this.type = type;
        this.primitiveType = Primitives.getForClass(type);
        this.parentType = parent;
    }

    public Optional<Type> getParent() {
        return Optional.ofNullable(parentType);
    }

    public ListProperty<Type> referencesTypesProperty() {
        return this.referencesTypes;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Primitives getPrimitiveType() {
        return this.primitiveType;
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    public boolean isNullable() {
        return this.type.isPrimitive(); //TODO: Think of a better solution or eliminate
    }

    public ListProperty<WeakReference<Instance>> getKnownInstances() {
        return this.knownInstances;
    }

    public static Type typeForClass(Class<?> cls) {
        Type t = Type.knownTypes.get(cls);
        if (t == null) {
            Class<?> superClass = cls.getSuperclass();
            Type superType = null;
            if (superClass != null) {
                superType = Type.typeForClass(superClass);
            }
            t = new Type(cls, superType);
            Type.knownTypes.put(cls, t);
        }
        return t;
    }

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }
}
