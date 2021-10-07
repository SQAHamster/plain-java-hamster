package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;

import java.util.*;

public class Type {
    private Class<?> type;
    private Type parentType;
    private List<Type> referencesTypes = new ArrayList<>();
    private Primitives primitiveType;

    private static Map<Class<?>, Type> knownTypes = new HashMap<>();

    protected Type(Class<?> type, Type parent) {
        this.type = type;
        this.primitiveType = Primitives.getForClass(type);
    }

    public Optional<Type> getParent() {
        return Optional.ofNullable(parentType);
    }

    public ObservableList<Type> getReferences() {
        return new ObservableListWrapper<>(Collections.unmodifiableList(referencesTypes));
    }

    public Class<?> getType() {
        return type;
    }

    public Primitives getPrimitiveType() {
        return this.primitiveType;
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
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
