package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.FieldViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.MethodViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ParamViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemberFactory {

    private final InspectionViewModel viewModel;

    static final int FIELD_RELOAD_INTERVAL = 1000;

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

    /**
     * Checks whether in the given configuration the given field/method should be accessible - and is it should it tries to do so.
     * <p>
     * A method/field should be accessible if:<br>
     * - It is public<br>
     * - It is protected and `setAccessible` is `true`<br>
     * - It is package private, the class of the member is in the same package as the given class of the instance and `setAccessible` is `true`<br>
     * - It is private, the class of the member is the same as the given class of the instance and `setAccessible` is `true`<br>
     *
     * @param member        The class member to check for accessibility and make accessible if needed
     * @param instanceClass The class of the instance this member is called from. Used to determine which members are allowed to be accessed.
     * @param setAccessible Whether to attempt to make non-public fields accessible or only use public fields that don't need `setAccessible` to be called on it
     * @param <T>           The type of member. Required to be able to return its modifiers and have an operation that tries to make it accessible. Either `Method` or `Field`
     * @return `true` iff the given member is accessible or has successfully been made accessible
     */
    <T extends AccessibleObject & Member> boolean checkAndMakeAccessible(final T member, final Class<?> instanceClass, final boolean setAccessible) {
        final Class<?> cls = member.getDeclaringClass();
        if (setAccessible) {
            if (cls.equals(instanceClass)) {
                return member.trySetAccessible();
            } else if (cls.getPackage().equals(instanceClass.getPackage())) {
                return !Modifier.isPrivate(member.getModifiers()) && member.trySetAccessible();
            } else {
                return (Modifier.isPublic(member.getModifiers()) || Modifier.isProtected(member.getModifiers())) && member.trySetAccessible();
            }
        } else {
            return Modifier.isPublic(member.getModifiers());
        }
    }

    ChangeListener<Boolean> createFieldReloadListener(final List<FieldViewModel> fields) {
        final ScheduledThreadPoolExecutor reloadTimer = new ScheduledThreadPoolExecutor(1);

        return new ChangeListener<>() {
            ScheduledFuture<?> runningTask = null;

            @Override
            public void changed(final ObservableValue<? extends Boolean> change, final Boolean oldVal, final Boolean newVal) {
                if (change.getValue()) {
                    // TODO: Find out why the values are sometimes old (?java mem model??)
                    MemberFactory.this.viewModel.executeOnMainThread(() -> fields.forEach(FieldViewModel::reloadValue));
                    if (this.runningTask == null) {
                        this.runningTask = reloadTimer.scheduleAtFixedRate(
                                () -> fields.forEach(FieldViewModel::reloadValue),
                                MemberFactory.FIELD_RELOAD_INTERVAL, MemberFactory.FIELD_RELOAD_INTERVAL, TimeUnit.MILLISECONDS);
                    }
                } else {
                    if (this.runningTask != null) {
                        this.runningTask.cancel(false);
                        this.runningTask = null;
                        fields.forEach(FieldViewModel::reloadValue);
                    }
                }
            }
        };
    }
}
