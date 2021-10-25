package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.FieldViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.MethodViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ParamViewModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemberFactory {

    private final InspectionViewModel viewModel;

    public MemberFactory(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Creates a new `MethodViewModel` instance for the method of a given instance or a static method if the given instance is `null`.
     * Adds all parameters, the return type as well as the invoke callback to the view model.
     *
     * @param instance The instance the method is on. `null` iff the method is static
     * @param method   The method to create the view model for. Can be any method (static/dynamic). Can't be `null`
     * @return The created view model for the method on the instance.
     */
    MethodViewModel createMethodViewModel(final Object instance, final Method method) {
        if (method == null) {
            throw new IllegalArgumentException("The method to create a view model for can't be null");
        }
        final Function<List<?>, Object> invokeMethod = params -> {
            try {
                return method.invoke(instance, params.toArray());
            } catch (final InvocationTargetException targetException) {
                throw ExecutionException.getForException(targetException);
            } catch (final IllegalAccessException e) {
                throw new IllegalArgumentException("Could not invoke method", e);
            }
        };
        return new MethodViewModel(method.getName(),
                Arrays.stream(method.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                new Type(method.getReturnType()),
                invokeMethod);
    }

    /**
     * Creates a new `FieldViewModel` instance for the field of a given instance or a static field if the given instance is `null`.
     * Also adds the reload function for the field
     *
     * @param instance The instance the method is on. `null` iff the method is static
     * @param field    The field to create the view model for. Can be any field (static/dynamic). Can't be `null`
     * @return The created view model for the field on the instance.
     */
    FieldViewModel createFieldViewModel(final Object instance, final Field field) {
        if (field == null) {
            throw new IllegalArgumentException("The field to create a view model for can't be null");
        }
        try {
            final FieldViewModel viewModel = new FieldViewModel(
                    field.getName(),
                    new Type(field.getType()),
                    field.get(instance),
                    Modifier.isFinal(field.getModifiers()),
                    () -> {
                        try {
                            return field.get(instance);
                        } catch (final IllegalAccessException e) {
                            throw new IllegalArgumentException("Cannot read field value", e);
                        }
                    }
            );
            viewModel.valueProperty().addListener((observable, oldValue, newValue) -> this.viewModel.executeOnMainThread(() -> {
                try {
                    field.set(instance, newValue);
                } catch (final IllegalAccessException e) {
                    throw new IllegalArgumentException("Could not set field", e);
                }
            }));
            return viewModel;
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot read field value", e);
        }
    }
}
