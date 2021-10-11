package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.*;
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
        if (obj == null) {
            throw new IllegalArgumentException("cannot get InstanceViewModel for null");
        }

        final InstanceViewModel newInstance = new InstanceViewModel(name,
                this.viewModel.viewModelForClass(obj.getClass()),
                Arrays.stream(obj.getClass().getDeclaredMethods())
                        .filter(method -> !Modifier.isStatic(method.getModifiers()))
                        .filter(method -> Modifier.isPublic(method.getModifiers())) //TODO improve this check
                        .map(method -> this.createInstanceMethodViewModel(obj, method))
                        .collect(Collectors.toList()),
                this.createSuperclassMethodViewModels(obj.getClass(), obj),
                Arrays.stream(obj.getClass().getDeclaredFields())
                        .filter(field -> !Modifier.isStatic(field.getModifiers()))
                        .filter(field -> Modifier.isPublic(field.getModifiers())) //TODO improve this check
                        .map(field -> this.createInstanceFieldViewModel(obj, field))
                        .collect(Collectors.toList()),
                this.createSuperclassFieldViewModels(obj.getClass(), obj),
                obj);
        this.viewModel.instancesProperty().add(newInstance);
        return newInstance;
    }

    private List<MethodViewModel> createSuperclassMethodViewModels(final Class<?> cls, final Object obj) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Collections.emptyList();
        }
        return Stream.concat(Arrays.stream(superclass.getDeclaredMethods())
                                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                                .filter(method -> Modifier.isPublic(method.getModifiers())) //TODO improve this check
                                .map(method -> this.createInstanceMethodViewModel(obj, method)),
                        this.createSuperclassMethodViewModels(superclass, obj).stream())
                .collect(Collectors.toList());
    }

    private List<FieldViewModel> createSuperclassFieldViewModels(final Class<?> cls, final Object obj) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Collections.emptyList();
        }
        return Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields())
                                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                                .filter(field -> Modifier.isPublic(field.getModifiers())) //TODO improve this check
                                .map(field -> this.createInstanceFieldViewModel(obj, field)),
                        this.createSuperclassFieldViewModels(superclass, obj).stream())
                .collect(Collectors.toList());
    }

    private MethodViewModel createInstanceMethodViewModel(final Object instance, final Method method) {
        final Function<List<?>, Object> invokeMethod = params -> {
            try {
                return method.invoke(instance, params.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        };
        return new MethodViewModel(method.toGenericString(),
                Arrays.stream(method.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                method.getReturnType(),
                invokeMethod);
    }

    private FieldViewModel createInstanceFieldViewModel(final Object instance, final Field field) {
        try {
            final FieldViewModel viewModel = new FieldViewModel(field.toGenericString(), new Type(field.getType()), field.get(instance));
            viewModel.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    field.set(instance, newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            ChangeListener<Boolean> isVisibleListener = (change, oldVal, newVal) -> {
                if (change.getValue()) {
                    try {
                        viewModel.valueProperty().setValue(field.get(instance));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            };
            viewModel.isVisibleProperty().addListener(isVisibleListener);
            return viewModel;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot read field value");
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
