package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record InstanceMethod(Instance instance,
                             Method method) {

    public Instance call(Instance ... params) {
        try {
            Object result = method.invoke(instance, Arrays.stream(params).map(Instance::getValue));
            if (result == null) {
                return null;
            } else {
                return new Instance(result, Type.typeForClass(result.getClass()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Type getReturnType() {
        return Type.typeForClass(method.getReturnType());
    }

    public List<Type> getParameterTypes() {
        return Arrays.stream(method.getParameterTypes()).map(Type::typeForClass).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return method.getName() + "(" +
                Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) +
                ")";
    }
}
