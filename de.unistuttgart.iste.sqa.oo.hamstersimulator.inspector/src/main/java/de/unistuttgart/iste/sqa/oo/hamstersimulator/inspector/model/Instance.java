package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record Instance(Object value, Type type) {

    public List<String> getAllMethods() {
        return Arrays.stream(value.getClass().getMethods()).map(Method::toGenericString).collect(Collectors.toList());
    }

    public Type getType() {
        return this.type;
    }

    public List<InstanceMethod> getMethods() {
        return Arrays.stream(this.type.getType().getMethods()).map(method -> new InstanceMethod(this, method)).collect(Collectors.toList());
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
