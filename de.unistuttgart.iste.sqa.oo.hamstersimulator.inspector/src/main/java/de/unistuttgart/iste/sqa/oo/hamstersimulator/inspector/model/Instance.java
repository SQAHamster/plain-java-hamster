package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Instance {

    static final WeakHashMap<Object, WeakReference<Instance>> knownObjects = new WeakHashMap<>();
    public static final Instance NULL_INSTANCE = new Instance(null, null);

    private final Object value;
    private final Type type;

    protected Instance(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    public List<String> getAllMethods() {
        return Arrays.stream(value.getClass().getMethods()).map(Method::toGenericString).collect(Collectors.toList());
    }

    public List<InstanceMethod> getMethods() {
        return Arrays.stream(this.type.getType().getMethods()).map(method -> new InstanceMethod(this, method)).collect(Collectors.toList());
    }

    public Object getValue() {
        return this.value;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return "null";
        } else if (this.type.getPrimitiveType() != Primitives.COMPLEX) {
            return value.toString();
        } else if (Arrays.stream(value.getClass().getDeclaredMethods()).anyMatch(method -> method.getName().equals("toString"))) {
            return value.toString();
        } else {
            return value.toString();
        }
    }
}
