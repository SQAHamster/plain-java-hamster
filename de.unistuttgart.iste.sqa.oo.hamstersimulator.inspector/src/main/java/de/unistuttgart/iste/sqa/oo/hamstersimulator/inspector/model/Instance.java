package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Instance {

    private static final Map<Object, Instance> knownObjects = new HashMap<>();
    private static final Instance NULL_INSTANCE = new Instance(null, null);

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

    public static Instance instanceForObject(Object obj) {
        if (obj == null) {
            return Instance.NULL_INSTANCE;
        }
        Instance i = Instance.knownObjects.get(obj);
        if (i == null) {
            i = new Instance(obj, Type.typeForClass(obj.getClass()));
            Instance.knownObjects.put(obj, i);
        }
        return i;
    }

    @Override
    public String toString() {
        if (this.type.getPrimitiveType() != Primitives.COMPLEX) {
            return value.toString();
        } else if (Arrays.stream(value.getClass().getDeclaredMethods()).anyMatch(method -> method.getName().equals("toString"))) {
            return value.toString();
        } else {
            return "[Object]";
        }
    }
}
