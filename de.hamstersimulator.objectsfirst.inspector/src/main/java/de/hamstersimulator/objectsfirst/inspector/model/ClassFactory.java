package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClassFactory {

    private final InspectionViewModel viewModel;
    private final IdentityHashMap<Class<?>, ClassViewModel> classViewModelLookup = new IdentityHashMap<>();

    public ClassFactory(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.classesProperty().addListener((ListChangeListener<ClassViewModel>) change -> {
            while (change.next()) {
                for (final ClassViewModel addedInfo : change.getAddedSubList()) {
                    this.classViewModelLookup.put(addedInfo.valueProperty().get(), addedInfo);
                }
                for (final ClassViewModel removedInfo : change.getRemoved()) {
                    this.classViewModelLookup.remove(removedInfo.valueProperty().get());
                }
            }
        });
    }

    public ClassViewModel viewModelForClass(final Class<?> cls) {
        return this.viewModelForClass(cls, false, false);
    }

    public ClassViewModel viewModelForClass(final Class<?> cls, final boolean setAccessible, final boolean setInstancesAccessible) {
        if (cls == null) {
            throw new IllegalArgumentException("cannot get ClassViewModel for null");
        }
        if (this.hasViewModelForClass(cls)) {
            final ClassViewModel viewModel = this.classViewModelLookup.get(cls);
            viewModel.setInstancesAccessibleProperty().set(setInstancesAccessible);
            if (setAccessible && !viewModel.hasPrivateMembersProperty().get()) {
                this.updateClassMemberListAccessible(cls, viewModel);
            }
            return viewModel;
        } else {
            final ClassViewModel newClass = this.createClassViewModel(cls, setAccessible, setInstancesAccessible);
            this.viewModel.classesProperty().add(newClass);
            return newClass;
        }
    }

    private void updateClassMemberListAccessible(final Class<?> cls, final ClassViewModel viewModel) {
        viewModel.constructorsProperty().clear();
        viewModel.constructorsProperty().addAll(Arrays.stream(cls.getConstructors())
                .filter(AccessibleObject::trySetAccessible)
                .map(this::createConstructorViewModel)
                .collect(Collectors.toList()));

        viewModel.staticMethodsProperty().clear();
        viewModel.staticMethodsProperty().addAll(Arrays.stream(cls.getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(AccessibleObject::trySetAccessible)
                .map(this::createStaticMethodViewModel)
                .collect(Collectors.toList()));

        viewModel.staticFieldsProperty().clear();
        viewModel.staticFieldsProperty().addAll(Arrays.stream(cls.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(AccessibleObject::trySetAccessible)
                .map(this::createStaticFieldViewModel)
                .collect(Collectors.toList()));

        viewModel.hasPrivateMembersProperty().set(true);
    }

    private ClassViewModel createClassViewModel(final Class<?> cls, final boolean setAccessible, final boolean setInstancesAccessible) {
        if (setInstancesAccessible) {
            System.out.println("Making class instances " + cls.getSimpleName() + " accessible");
        }

        if (setAccessible) {
            System.out.println("Making class " + cls.getSimpleName() + " accessible");
        }

        return new ClassViewModel(cls.getSimpleName(),
                Arrays.stream(cls.getConstructors())
                        .filter(constructor -> {
                            if (setAccessible) {
                                return constructor.trySetAccessible();
                            } else {
                                return Modifier.isPublic(constructor.getModifiers());
                            }
                        })
                        .map(this::createConstructorViewModel)
                        .collect(Collectors.toCollection(ArrayList::new)),
                Arrays.stream(cls.getMethods())
                        .filter(method -> Modifier.isStatic(method.getModifiers()))
                        .filter(method -> {
                            if (setAccessible) {
                                return method.trySetAccessible();
                            } else {
                                return Modifier.isPublic(method.getModifiers());
                            }
                        })
                        .map(this::createStaticMethodViewModel)
                        .collect(Collectors.toCollection(ArrayList::new)),
                Arrays.stream(cls.getFields())
                        .filter(field -> Modifier.isStatic(field.getModifiers()))
                        .filter(field -> {
                            if (setAccessible) {
                                return field.trySetAccessible();
                            } else {
                                return Modifier.isPublic(field.getModifiers());
                            }
                        })
                        .map(this::createStaticFieldViewModel)
                        .collect(Collectors.toCollection(ArrayList::new)),
                cls,
                setInstancesAccessible,
                setAccessible
        );
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classViewModelLookup.containsKey(cls);
    }

    private MethodViewModel createStaticMethodViewModel(final Method method) {
        final Function<List<?>, Object> invokeMethod = params -> {
            try {
                return method.invoke(null, params.toArray());
            } catch (final InvocationTargetException targetEx) {
                final Throwable cause = targetEx.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else {
                    throw new RuntimeException("A non-RuntimeException was thrown. Message: " + cause.getMessage(), cause);
                }
            } catch (final IllegalAccessException e) {
                throw new IllegalArgumentException("Could not invoke static method", e);
            }
        };
        return new MethodViewModel(method.getName(),
                Arrays.stream(method.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                new Type(method.getReturnType()),
                invokeMethod);
    }

    private MethodViewModel createConstructorViewModel(final Constructor<?> constructor) {
        final Function<List<?>, ?> construct = params -> {
            try {
                return constructor.newInstance(params.toArray());
            } catch (final InvocationTargetException targetEx) {
                final Throwable cause = targetEx.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else {
                    throw new RuntimeException("A non-RuntimeException was thrown. Message: " + cause.getMessage(), cause);
                }
            } catch (final IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException("Could not invoke constructor", e);
            }
        };
        return new MethodViewModel("",
                Arrays.stream(constructor.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                new Type(constructor.getDeclaringClass()),
                construct);
    }

    private FieldViewModel createStaticFieldViewModel(final Field field) {
        try {
            final FieldViewModel viewModel = new FieldViewModel(field.getName(), new Type(field.getType()),
                    field.get(null), Modifier.isFinal(field.getModifiers()));
            viewModel.valueProperty().addListener((observable, oldValue, newValue) -> this.viewModel.executeOnMainThread(() -> {
                try {
                    field.set(null, newValue);
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException("Could not set field", e);
                }
            }));
            final ChangeListener<Boolean> isVisibleListener = (change, oldVal, newVal) -> {
                if (change.getValue()) {
                    try {
                        viewModel.valueProperty().setValue(field.get(null));
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException("Could not get field value", e);
                    }
                }
            };
            viewModel.isVisibleProperty().addListener(isVisibleListener);
            return viewModel;
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot read field value", e);
        }
    }
}
