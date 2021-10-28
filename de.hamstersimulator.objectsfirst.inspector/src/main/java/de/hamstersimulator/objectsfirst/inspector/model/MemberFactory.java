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
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MemberFactory {

    private final InspectionViewModel viewModel;
    private final ScheduledThreadPoolExecutor reloadTimer;

    private static final int FIELD_RELOAD_INTERVAL = 1000;

    public MemberFactory(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
        this.reloadTimer = new ScheduledThreadPoolExecutor(1, MemberFactory::createReloadTimerThread);
    }

    /**
     * Factory method to create new thread for the reload timer thread pool from the given executable.
     * <p>
     * All created threads have the common name prefix "reloadTimer-" and are Daemon threads,
     * so they don't keep the program from exiting when the simulator is closed
     *
     * @param runnable The runnable the tread is supposed to execute. Can't be null
     * @return A new (not yet started) thread that has been made a daemon thread with a name starting with
     * "<code>reloadTimer-</code>"
     * @throws IllegalArgumentException Iff <code>runnable</code> is null
     */
    private static Thread createReloadTimerThread(final Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable for reload timer thread was null");
        }
        final Thread thread = new Thread(runnable);
        thread.setName("reloadTimer-" + thread.getName());
        thread.setDaemon(true);
        return thread;
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

    private void fieldViewModelValueChanged(final FieldViewModel fieldViewModel, final Field field) {

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
        final Supplier<Object> fieldValueGetter = () -> {
            try {
                return field.get(instance);
            } catch (final IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot read field value", e);
            }
        };
        final FieldViewModel newFieldViewModel = new FieldViewModel(
                field.getName(),
                new Type(field.getType()),
                fieldValueGetter.get(),
                Modifier.isFinal(field.getModifiers()),
                fieldValueGetter
        );
        newFieldViewModel.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newFieldViewModel.isChangedByGui()) {
                this.viewModel.executeOnMainThread(() -> {
                    try {
                        field.set(instance, newValue);
                    } catch (final IllegalAccessException e) {
                        throw new IllegalArgumentException("Could not set field", e);
                    }
                });
            }
        });
        return newFieldViewModel;
    }

    /**
     * Checks whether in the given configuration the given field/method should be accessible - and is it should it tries to do so.
     * <p>
     * A method/field should be accessible if it is NOT synthetic AND if:<br>
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
        if (member.isSynthetic()) {
            return false;
        }
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

    /**
     * Returns a boolean listener that - when activated with `true` - starts reloading the field values of
     * all the fields in the given list in a regular interval specified by `FIELD_RELOAD_INTERVAL`.
     * <p>
     * Once activated with `false` the refreshing will be stopped.
     * <p>
     * The reloading will be done on a separate daemon thread in the <code>reloadTimer</code>.
     * Except for the first load which is done on the calling thread
     *
     * @param fields The fields which to refresh when the listener was called with <code>true</code> until it is called with <code>false</code>
     * @return The change listener for reloading
     */
    ChangeListener<Boolean> createFieldReloadListener(final List<FieldViewModel> fields) {
        return new FieldTimedReloadListener(fields, this.reloadTimer);
    }

    /**
     * A listener class which supplies the logic for <code>createFieldReloadListener</code>
     *
     * @see MemberFactory#createFieldReloadListener
     */
    private static class FieldTimedReloadListener implements ChangeListener<Boolean> {

        /**
         * If a reload timer is currently running this is the <code>ScheduledFuture</code> to interact (specifically cancel) with it.
         * <p>
         * If none is running, this will be null. (It's not an Optional, as optionals as class property are considered bad practice)
         */
        private ScheduledFuture<?> runningTask = null;

        /**
         * The list of <code>FieldViewModel</code>s which need to be refreshed each time the timer is scheduled.
         * <p>
         * Can't be null
         */
        private final List<FieldViewModel> fields;

        /**
         * The <code>ScheduledThreadPoolExecutor</code> to schedule the timer reloads on.
         * The actual reloading (for evey but the initial and last reload) will happen on the thread ths executor assigns the task to.
         * <p>
         * Can't be null
         */
        private final ScheduledThreadPoolExecutor reloadTimer;

        /**
         * Initializes a new <code>FieldTimedReloadListener</code> which reloads all the fields in the provided list on the given executor once activated
         *
         * @param fields      The list of <code>FieldViewModel</code>s which need to be refreshed each time the timer is scheduled.
         *                    Can't be null
         * @param reloadTimer The <code>ScheduledThreadPoolExecutor</code> to schedule the timer reloads on.
         *                    The actual reloading (for evey but the initial and last reload) will happen on the thread ths executor assigns the task to.
         *                    Can't be null
         */
        FieldTimedReloadListener(final List<FieldViewModel> fields, final ScheduledThreadPoolExecutor reloadTimer) {
            if (fields == null) {
                throw new IllegalArgumentException("List of fields to reload can't be null");
            }
            if (reloadTimer == null) {
                throw new IllegalArgumentException("The scheduled executor to run the reloads on can't be null");
            }
            this.fields = fields;
            this.reloadTimer = reloadTimer;
        }

        @Override
        public void changed(final ObservableValue<? extends Boolean> change, final Boolean oldValue, final Boolean newValue) {
            this.fields.forEach(FieldViewModel::reloadValue);
            if (change.getValue()) {
                if (this.runningTask == null) {
                    this.runningTask = this.reloadTimer.scheduleAtFixedRate(
                            () -> this.fields.forEach(FieldViewModel::reloadValue),
                            MemberFactory.FIELD_RELOAD_INTERVAL,
                            MemberFactory.FIELD_RELOAD_INTERVAL,
                            TimeUnit.MILLISECONDS);
                }
            } else if (this.runningTask != null) {
                this.runningTask.cancel(false);
                this.runningTask = null;
            }
        }
    }
}