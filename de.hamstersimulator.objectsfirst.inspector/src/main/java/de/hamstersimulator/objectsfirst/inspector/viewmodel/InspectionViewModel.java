package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.inspector.model.*;
import javafx.beans.binding.BooleanBinding;
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
    private final SimpleObjectProperty<InspectionExecutor> executor = new SimpleObjectProperty<>(this, "executor");
    private final Map<Class<?>, Type> enumInputTypeLookup = new HashMap<>();
    final InstanceFactory instanceFactory;
    final ClassFactory classFactory;
    final MemberFactory memberFactory;
    private final ClassInstanceManager classInstanceManager;

    public InspectionViewModel() {
        this.classes = new ReadOnlyListWrapper<>(this, "classes", FXCollections.observableList(new ArrayList<>()));
        this.instances = new SimpleListProperty<>(this, "objects", FXCollections.observableList(new ArrayList<>()));
        this.multiTypes.add(new Type(Object.class));
        this.multiTypes.addAll(Stream.of(String.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class)
                .map(Type::new).collect(Collectors.toList()));

        this.classes.addListener(this::onClassesListChanged);

        this.memberFactory = new MemberFactory(this);
        this.instanceFactory = new InstanceFactory(this, this.memberFactory);
        this.classFactory = new ClassFactory(this, this.memberFactory);
        this.classInstanceManager = new ClassInstanceManager(this);
    }

    /**
     * Adds all enum types in the given list to the list of known enums
     * (<code>multiTypes</code> and <code>enumInputTypeLookup</code>)
     *
     * @param classesToAdd A non-null list which to check if they are enums and potentially add
     */
    private void addToMultiTypesIfEnum(final List<? extends ClassViewModel> classesToAdd) {
        if (classesToAdd == null) {
            throw new IllegalArgumentException("List of classes can't be null");
        }
        for (final ClassViewModel addedInfo : classesToAdd) {
            final Class<?> cls = addedInfo.valueProperty().get();
            if (Enum.class.isAssignableFrom(cls)) {
                final Type type = new Type(cls);
                this.enumInputTypeLookup.put(cls, type);
                this.multiTypes.add(type);
            }
        }
    }

    /**
     * Removes the classes from <code>multiTypes</code> and <code>enumInputTypeLookup</code> if they were on them.
     *
     * @param classesToRemove A non-null list of class view models for which to remove the class from the list of known enums
     */
    private void removeFromMultiTypesIfEnum(final List<? extends ClassViewModel> classesToRemove) {
        if (classesToRemove == null) {
            throw new IllegalArgumentException("List of classes can't be null");
        }
        for (final ClassViewModel removedInfo : classesToRemove) {
            final Class<?> cls = removedInfo.valueProperty().get();
            if (this.enumInputTypeLookup.containsKey(cls)) {
                this.multiTypes.remove(this.enumInputTypeLookup.remove(cls));
            }
        }
    }

    /**
     * Listener function for changes in a classes list.
     * <p>
     * Iterates over all changes and for added classes adds them to the known enums if it is a valid enum.
     * For removed classes if they were in the enum list they will be removed
     *
     * @param change All changes that happened to the list. Must be non-null
     */
    private void onClassesListChanged(final ListChangeListener.Change<? extends ClassViewModel> change) {
        if (change == null) {
            throw new IllegalArgumentException("List of classes can't be null");
        }
        while (change.next()) {
            this.addToMultiTypesIfEnum(change.getAddedSubList());
            this.removeFromMultiTypesIfEnum(change.getRemoved());
        }
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

    public ClassInstanceManager getClassInstanceManager() {
        return this.classInstanceManager;
    }

    public BooleanBinding isReadOnly() {
        return this.executor.isNull();
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

    public ClassViewModel viewModelForClass(final Class<?> cls, final boolean setAccessible, final boolean setInstancesAccessible) {
        return this.classFactory.viewModelForClass(cls, setAccessible, setInstancesAccessible);
    }

    public boolean hasViewModelForClass(final Class<?> cls) {
        return this.classFactory.hasViewModelForClass(cls);
    }

    /*@
     @ requires isReadOnly().get();
     @ requires executor != null;
     @ ensures !isReadOnly().get();
     @*/

    /**
     * Sets the current executor
     * Must be executed on the JavaFX thread
     *
     * @param executor the new executor used to execute runnables on the main thread
     */
    public void setExecutor(final InspectionExecutor executor) {
        if (!this.isReadOnly().get()) {
            throw new IllegalStateException("There is already an executor");
        }
        this.executor.set(executor);
    }

    /*@
     @ requires !isReadOnly().get();
     @ ensures isReadOnly().get();
     @*/

    /**
     * Removes the current executor
     * Must be executed on the JavaFX thread
     */
    public void removeExecutor() {
        if (this.isReadOnly().get()) {
            throw new IllegalStateException("There is currently no executor");
        }
        this.executor.set(null);
    }

    /*@
     @ requires !isReadOnly().get();
     @ requires runnable != null;
     @*/

    /**
     * Executes a runnable on the main thread
     * This is not blocking!
     *
     * @param runnable executed on the main thread
     */
    public void executeOnMainThread(final Runnable runnable) {
        if (this.isReadOnly().get()) {
            throw new IllegalStateException("There is currently no executor");
        }
        this.executor.get().scheduleRunnable(runnable);
    }

    /**
     * Stops the execution
     */
    public void stopExecution() {
        if (!this.isReadOnly().get()) {
            this.executor.get().stop();
        }
    }
}