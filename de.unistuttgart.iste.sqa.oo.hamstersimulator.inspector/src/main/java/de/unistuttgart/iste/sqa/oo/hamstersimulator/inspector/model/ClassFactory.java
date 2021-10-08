package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.*;
import javafx.collections.ListChangeListener;
import java.util.Collections;
import java.util.IdentityHashMap;

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
        return new ClassViewModel<>(cls.getName(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                cls);
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classViewModelLookup.containsKey(cls);
    }
}
