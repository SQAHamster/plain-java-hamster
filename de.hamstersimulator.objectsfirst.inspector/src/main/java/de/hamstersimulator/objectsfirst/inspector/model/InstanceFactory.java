package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class InstanceFactory {

    private final InspectionViewModel viewModel;
    private final IdentityHashMap<Object, InstanceViewModel> instanceViewModelLookup = new IdentityHashMap<>();

    public InstanceFactory(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.instancesProperty().addListener((ListChangeListener<InstanceViewModel>) change -> {
            while (change.next()) {
                for (final InstanceViewModel addedInfo : change.getAddedSubList()) {
                    this.instanceViewModelLookup.put(addedInfo.valueProperty().get(), addedInfo);
                }
                for (final InstanceViewModel removedInfo : change.getRemoved()) {
                    this.instanceViewModelLookup.remove(removedInfo.valueProperty().get());
                }
            }
        });
    }

    public InstanceViewModel createInstanceViewModel(final Object obj, final String name) {
        return this.createInstanceViewModel(obj, name, false);
    }

    public InstanceViewModel createInstanceViewModel(final Object obj, final String name, boolean setAccessible) {
        if (obj == null) {
            throw new IllegalArgumentException("cannot get InstanceViewModel for null");
        }

        Class<?> cls = obj.getClass();
        ClassViewModel clsViewModel = this.viewModel.viewModelForClass(cls, setAccessible, setAccessible);
        boolean setAccessibleValue = setAccessible || clsViewModel.setInstancesAccessibleProperty().get();

        if (setAccessibleValue) {
            System.out.println("Making instance " + name + " accessible");
        }

        final InstanceViewModel newInstance = new InstanceViewModel(name,
                clsViewModel,
                this.createMethodViewModelsForObject(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createSuperclassMethodViewModels(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createFieldViewModelsForObject(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createSuperclassFieldViewModels(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                obj);
        this.viewModel.instancesProperty().add(newInstance);
        return newInstance;
    }

    private Stream<MethodViewModel> createMethodViewModelsForObject(final Class<?> cls, final Object obj, boolean setAccessible) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> {
                    if (setAccessible) {
                        return method.trySetAccessible();
                    } else {
                        return Modifier.isPublic(method.getModifiers());
                    }
                }) //TODO improve this check, potentially reintroduce
                .map(method -> this.createInstanceMethodViewModel(obj, method));
    }

    private Stream<FieldViewModel> createFieldViewModelsForObject(final Class<?> cls, final Object obj, boolean setAccessible) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> {
                    if (setAccessible) {
                        return field.trySetAccessible();
                    } else {
                        return Modifier.isPublic(field.getModifiers());
                    }
                }) //TODO improve this check
                .map(field -> this.createInstanceFieldViewModel(obj, field));
    }

    private Stream<MethodViewModel> createSuperclassMethodViewModels(final Class<?> cls, final Object obj, boolean setAccessible) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Stream.empty();
        }
        return Stream.concat(this.createMethodViewModelsForObject(superclass, obj, setAccessible),
                        this.createSuperclassMethodViewModels(superclass, obj, setAccessible));
    }

    private Stream<FieldViewModel> createSuperclassFieldViewModels(final Class<?> cls, final Object obj, boolean setAccessible) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Stream.empty();
        }
        return Stream.concat(this.createFieldViewModelsForObject(superclass, obj, setAccessible),
                        this.createSuperclassFieldViewModels(superclass, obj, setAccessible));
    }

    private MethodViewModel createInstanceMethodViewModel(final Object instance, final Method method) {
        final Function<List<?>, Object> invokeMethod = params -> {
            try {
                return method.invoke(instance, params.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Could not invoke method", e); //TODO maybe rethrow causing exception
            }
        };
        return new MethodViewModel(method.getName(),
                Arrays.stream(method.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                new Type(method.getReturnType()),
                invokeMethod);
    }

    private FieldViewModel createInstanceFieldViewModel(final Object instance, final Field field) {
        try {
            final FieldViewModel viewModel = new FieldViewModel(field.getName(), new Type(field.getType()),
                    field.get(instance), Modifier.isFinal(field.getModifiers()));
            viewModel.valueProperty().addListener((observable, oldValue, newValue) -> {
                this.viewModel.executeOnMainThread(() -> {
                    try {
                        field.set(instance, newValue);
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Could not set field", e);
                    }
                });
            });
            ChangeListener<Boolean> isVisibleListener = (change, oldVal, newVal) -> {
                if (change.getValue()) { //TODO: Spawn/kill refresh timer
                    try {
                        viewModel.valueProperty().setValue(field.get(instance));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Could not get field value", e);
                    }
                }
            };
            viewModel.isVisibleProperty().addListener(isVisibleListener);
            return viewModel;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot read field value",  e);
        }
    }

    public Optional<InstanceViewModel> getViewModelForObject(final Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("cannot get InstanceViewModel for null");
        }

        return Optional.ofNullable(this.instanceViewModelLookup.get(obj));
    }

    public boolean hasViewModelForObject(final Object object) {
        return this.instanceViewModelLookup.containsKey(object);
    }
}
