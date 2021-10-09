package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.ClassFactory;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.InstanceFactory;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata.B;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata.C;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InspectionViewModel {
    private final ReadOnlyListWrapper<ClassViewModel> classes;
    private final SimpleListProperty<InstanceViewModel> instances;
    private final SimpleListProperty<Type> multiTypes
            = new SimpleListProperty<>(this, "multiInputTypes", FXCollections.observableList(new ArrayList<>()));
    private final Map<Class<?>, Type> enumInputTypeLookup = new HashMap<>();
    private final InstanceFactory instanceFactory;
    private final ClassFactory classFactory;

    public InspectionViewModel(final HamsterGame hamsterGame) {
        this.classes = new ReadOnlyListWrapper<>(this, "classes", FXCollections.observableList(new ArrayList<>()));
        this.instances = new SimpleListProperty<>(this, "objects", FXCollections.observableList(new ArrayList<>()));
        this.multiTypes.add(Type.OBJECT_TYPE);
        this.multiTypes.addAll(Stream.of(String.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class)
                .map(Type::new).collect(Collectors.toList()));

        this.classes.addListener((ListChangeListener<ClassViewModel>) change -> {
            while(change.next()) {
                for (final ClassViewModel addedInfo : change.getAddedSubList()) {
                    final Class<?> cls = addedInfo.valueProperty().get();
                    if (Enum.class.isAssignableFrom(cls)) {
                        final Type type = new Type(cls);
                        this.enumInputTypeLookup.put(cls, type);
                        this.multiTypes.add(type);
                    }
                }
                for (final ClassViewModel removedInfo: change.getRemoved()) {
                    final Class<?> cls = removedInfo.valueProperty().get();
                    if (this.enumInputTypeLookup.containsKey(cls)) {
                        this.multiTypes.remove(this.enumInputTypeLookup.remove(cls));
                    }
                }
            }
        });

        this.instanceFactory = new InstanceFactory(this);
        this.classFactory = new ClassFactory(this);

        this.viewModelForClass(hamsterGame.getClass());
        this.viewModelForClass(SimpleHamsterGame.class);
        this.viewModelForClass(Hamster.class);
        final InstanceViewModel hamsterGameViewModel = this.createInstanceViewModel(hamsterGame, "hamsterGame");
    }

    public ReadOnlyListProperty<ClassViewModel> classesProperty() {
        return this.classes.getReadOnlyProperty();
    }

    public ListProperty<InstanceViewModel> instancesProperty() {
        return this.instances;
    }

    public ListProperty<Type> multiInputTypesProperty() {
        return this.multiTypes;
    }

    public Optional<InstanceViewModel> getViewModelForObject(final Object object) {
        return this.instanceFactory.getViewModelForObject(object);
    }

    public InstanceViewModel createInstanceViewModel(final Object obj, final String name) {
        return this.instanceFactory.createInstanceViewModel(obj, name);
    }

    public boolean hasViewModelForObject(final Object object) {
        return this.instanceFactory.hasViewModelForObject(object);
    }
    
    public ClassViewModel viewModelForClass(final Class<?> cls) {
        return this.classFactory.viewModelForClass(cls);
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classFactory.hasViewModelForClass(cls);
    }
}