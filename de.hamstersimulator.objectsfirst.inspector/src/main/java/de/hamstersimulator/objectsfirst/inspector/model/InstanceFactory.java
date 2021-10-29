package de.hamstersimulator.objectsfirst.inspector.model;

import de.hamstersimulator.objectsfirst.inspector.viewmodel.*;
import javafx.collections.ListChangeListener;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class InstanceFactory {

    private final InspectionViewModel viewModel;
    private final MemberFactory memberFactory;
    private final IdentityHashMap<Object, InstanceViewModel> instanceViewModelLookup = new IdentityHashMap<>();

    public InstanceFactory(final InspectionViewModel viewModel, final MemberFactory memberFactory) {
        this.viewModel = viewModel;
        this.memberFactory = memberFactory;
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

    public void updateInstanceMemberListAccessible(final InstanceViewModel viewModel) {
        if (!viewModel.hasPrivateMembersProperty().get()) {
            final Object obj = viewModel.valueProperty().get();
            final Class<?> cls = obj.getClass();
            viewModel.methodsProperty().clear();
            viewModel.methodsProperty().addAll(this.createMethodViewModelsForObject(cls, obj, true).collect(Collectors.toList()));
            viewModel.superclassMethodsProperty().clear();
            viewModel.superclassMethodsProperty().addAll(this.createSuperclassMethodViewModels(cls, obj, true).collect(Collectors.toList()));
            viewModel.fieldsProperty().clear();
            viewModel.fieldsProperty().addAll(this.createFieldViewModelsForObject(cls, obj, true).collect(Collectors.toList()));
            viewModel.superclassFieldsProperty().clear();
            viewModel.superclassFieldsProperty().addAll(this.createSuperclassFieldViewModels(cls, obj, true).collect(Collectors.toList()));
            viewModel.hasPrivateMembersProperty().set(true);
        }
    }

    public InstanceViewModel createInstanceViewModel(final Object obj, final String name) {
        return this.createInstanceViewModel(obj, name, false);
    }

    public InstanceViewModel createInstanceViewModel(final Object obj, final String name, final boolean setAccessible) {
        if (obj == null) {
            throw new IllegalArgumentException("cannot get InstanceViewModel for null");
        }

        final Class<?> cls = obj.getClass();
        final ClassViewModel clsViewModel = this.viewModel.viewModelForClass(cls, setAccessible, setAccessible);
        final boolean setAccessibleValue = setAccessible || clsViewModel.setInstancesAccessibleProperty().get();

        final InstanceViewModel newInstance = new InstanceViewModel(name,
                clsViewModel,
                this.createMethodViewModelsForObject(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createSuperclassMethodViewModels(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createFieldViewModelsForObject(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                this.createSuperclassFieldViewModels(cls, obj, setAccessibleValue).collect(Collectors.toCollection(ArrayList::new)),
                obj,
                setAccessibleValue);

        newInstance.isVisibleProperty().addListener(
                this.memberFactory.createFieldReloadListener(newInstance.fieldsProperty())
        );
        this.viewModel.instancesProperty().add(newInstance);
        return newInstance;
    }

    private Stream<MethodViewModel> createMethodViewModelsForObject(final Class<?> cls, final Object obj, final boolean setAccessible) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> this.memberFactory.checkAndMakeAccessible(method, obj.getClass(), setAccessible))
                .map(method -> this.memberFactory.createMethodViewModel(obj, method));
    }

    private Stream<FieldViewModel> createFieldViewModelsForObject(final Class<?> cls, final Object obj, final boolean setAccessible) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> this.memberFactory.checkAndMakeAccessible(field, obj.getClass(), setAccessible))
                .map(field -> this.memberFactory.createFieldViewModel(obj, field));
    }

    private Stream<MethodViewModel> createSuperclassMethodViewModels(final Class<?> cls, final Object obj, final boolean setAccessible) {
        final Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Stream.empty();
        }
        return Stream.concat(this.createMethodViewModelsForObject(superclass, obj, setAccessible),
                this.createSuperclassMethodViewModels(superclass, obj, setAccessible));
    }

    private Stream<FieldViewModel> createSuperclassFieldViewModels(final Class<?> cls, final Object obj, final boolean setAccessible) {
        final Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return Stream.empty();
        }
        return Stream.concat(this.createFieldViewModelsForObject(superclass, obj, setAccessible),
                this.createSuperclassFieldViewModels(superclass, obj, setAccessible));
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
