package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClassFactory {

    private final InspectionViewModel viewModel;
    private final IdentityHashMap<Class, ClassViewModel<?>> classViewModelLookup = new IdentityHashMap<>();

    public ClassFactory(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.classesProperty().addListener((ListChangeListener<ClassViewModel<?>>) change -> {
            while(change.next()) {
                for (final ClassViewModel<?> addedInfo : change.getAddedSubList()) {
                    this.classViewModelLookup.put(addedInfo.valueProperty().get(), addedInfo);
                }
                for (final ClassViewModel<?> removedInfo : change.getRemoved()) {
                    this.classViewModelLookup.remove(removedInfo.valueProperty().get());
                }
            }
        });
    }

    public <T> ClassViewModel<T> viewModelForClass(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("cannot get ClassViewModel for null");
        }
        if (this.hasViewModelForClass(cls)) {
            return (ClassViewModel<T>) this.classViewModelLookup.get(cls);
        } else {
            ClassViewModel<T> newClass = this.createClassViewModel(cls);
            this.viewModel.classesProperty().add(newClass);
            return newClass;
        }
    }

    private <T> ClassViewModel<T> createClassViewModel(Class<T> cls) {
        return new ClassViewModel<T>(cls.getName(),
                Arrays.stream((Constructor<T>[])cls.getConstructors())
                        .filter(AccessibleObject::trySetAccessible)
                        .map(this::createConstructorViewModel)
                        .collect(Collectors.toList()),
                Arrays.stream(cls.getMethods())
                        .filter(method -> Modifier.isStatic(method.getModifiers()))
                        .filter(method -> Modifier.isPublic(method.getModifiers())) //TODO improve this check,
                        .map(this::createStaticMethodViewModel)
                                .collect(Collectors.toList()),
                Arrays.stream(cls.getFields())
                        .filter(method -> Modifier.isStatic(method.getModifiers()))
                        .filter(method -> Modifier.isPublic(method.getModifiers())) //TODO improve this check,
                        .map(this::createStaticFieldViewModel)
                        .collect(Collectors.toList()),
                cls);
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classViewModelLookup.containsKey(cls);
    }

    private MethodViewModel<?> createStaticMethodViewModel(final Method method) {
        final Function<List<?>, Object> invokeMethod = params -> {
            try {
                return method.invoke(null, params.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        };
        return new MethodViewModel<>(method.toGenericString(),
                Arrays.stream(method.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                method.getReturnType(),
                invokeMethod);
    }

    private <T> ConstructorViewModel<T> createConstructorViewModel(final Constructor<T> constructor) {
        final Function<List<?>, T> construct = params -> {
            try {
                return constructor.newInstance(params.toArray());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        };
        return new ConstructorViewModel<T>(constructor.toGenericString(),
                Arrays.stream(constructor.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                constructor.getDeclaringClass(),
                construct);
    }

    private FieldViewModel createStaticFieldViewModel(final Field field) {
        try {
            final FieldViewModel viewModel = new FieldViewModel(field.toGenericString(), field.getType(), field.get(null));
            viewModel.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    field.set(null, newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            ChangeListener<Boolean> isVisibleListener = (change, oldVal, newVal) -> {
                if (change.getValue()) {
                    try {
                        viewModel.valueProperty().setValue(field.get(null));
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
}
