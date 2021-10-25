package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClassFactory {

    private final InspectionViewModel viewModel;
    private final MemberFactory memberFactory;
    private final IdentityHashMap<Class<?>, ClassViewModel> classViewModelLookup = new IdentityHashMap<>();

    public ClassFactory(final InspectionViewModel viewModel, final MemberFactory memberFactory) {
        this.viewModel = viewModel;
        this.memberFactory = memberFactory;
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
                .map(method -> this.memberFactory.createMethodViewModel(null, method))
                .collect(Collectors.toList()));

        viewModel.staticFieldsProperty().clear();
        viewModel.staticFieldsProperty().addAll(Arrays.stream(cls.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(AccessibleObject::trySetAccessible)
                .map(field -> this.memberFactory.createFieldViewModel(null, field))
                .collect(Collectors.toList()));

        viewModel.hasPrivateMembersProperty().set(true);
    }

    private ClassViewModel createClassViewModel(final Class<?> cls, final boolean setAccessible, final boolean setInstancesAccessible) {
        final ClassViewModel viewModel = new ClassViewModel(cls.getSimpleName(),
                Arrays.stream(cls.getConstructors())
                        .filter(constructor -> this.memberFactory.checkAndMakeAccessible(constructor, cls, setAccessible))
                        .map(this::createConstructorViewModel)
                        .collect(Collectors.toCollection(ArrayList::new)),
                Arrays.stream(cls.getMethods())
                        .filter(method -> Modifier.isStatic(method.getModifiers()))
                        .filter(method -> this.memberFactory.checkAndMakeAccessible(method, cls, setAccessible))
                        .map(method -> this.memberFactory.createMethodViewModel(null, method))
                        .collect(Collectors.toCollection(ArrayList::new)),
                Arrays.stream(cls.getFields())
                        .filter(field -> Modifier.isStatic(field.getModifiers()))
                        .filter(field -> this.memberFactory.checkAndMakeAccessible(field, cls, setAccessible))
                        .map(field -> this.memberFactory.createFieldViewModel(null, field))
                        .collect(Collectors.toCollection(ArrayList::new)),
                cls,
                setInstancesAccessible,
                setAccessible
        );
        final ChangeListener<Boolean> isVisibleListener = (change, oldVal, newVal) -> {
            if (change.getValue()) {
                viewModel.staticFieldsProperty().forEach(FieldViewModel::reloadValue);
            }
        };
        viewModel.isVisibleProperty().addListener(isVisibleListener);
        return viewModel;
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classViewModelLookup.containsKey(cls);
    }

    private MethodViewModel createConstructorViewModel(final Constructor<?> constructor) {
        final Function<List<?>, ?> construct = params -> {
            try {
                return constructor.newInstance(params.toArray());
            } catch (final InvocationTargetException targetException) {
                throw ExecutionException.getForException(targetException);
            } catch (final IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException("Could not invoke constructor", e);
            }
        };
        return new MethodViewModel("",
                Arrays.stream(constructor.getParameters()).map(ParamViewModel::fromParameter).collect(Collectors.toList()),
                new Type(constructor.getDeclaringClass()),
                construct);
    }
}
