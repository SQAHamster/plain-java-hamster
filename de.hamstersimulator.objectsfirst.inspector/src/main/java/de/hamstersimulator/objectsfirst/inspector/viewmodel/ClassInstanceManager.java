package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;

public class ClassInstanceManager {

    private final InspectionViewModel viewModel;

    ClassInstanceManager(final InspectionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public <T> void addClass(final Class<T> cls, final boolean setAccessible) {
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.viewModel.classFactory.viewModelForClass(cls, setAccessible, setAccessible));
    }

    public void addInstance(final Object instance, final String name, final boolean setAccessible) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance to be added can't be null");
        }
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            final Optional<InstanceViewModel> instanceViewModel = this.viewModel.getViewModelForObject(instance);
            if (instanceViewModel.isEmpty()) {
                this.viewModel.instanceFactory.createInstanceViewModel(instance, name, setAccessible);
            } else {
                instanceViewModel.get().nameProperty().set(name);
                if (setAccessible && !instanceViewModel.get().hasPrivateMembersProperty().get()) {
                    this.viewModel.instanceFactory.updateInstanceMemberListAccessible(instanceViewModel.get());
                }
            }
        });
    }

    public <T> void addClass(final Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class to be added can't be null");
        }
        final Class<?> caller = this.getCallerClass(Thread.currentThread().getStackTrace());
        this.addClass(cls, caller.getPackage().equals(cls.getPackage()));
    }

    public void addInstance(final Object instance, final String name) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance to be added can't be null");
        }
        final Class<?> caller = this.getCallerClass(Thread.currentThread().getStackTrace());
        this.addInstance(instance, name, caller.getPackage().equals(instance.getClass().getPackage()));
    }

    public void addClassesFromClassPackage(final Class<?> cls) {
        final ClassLoader loader = cls.getClassLoader();
        final String packageName = cls.getPackageName();
        final InputStream classesInPackage = loader.getResourceAsStream(packageName.replaceAll("[.]", "/"));
        if (classesInPackage == null) {
            throw new IllegalStateException("No classes found in class loader");
        }
        final BufferedReader classNameReader = new BufferedReader(new InputStreamReader(classesInPackage));
        classNameReader.lines()
                .filter(l -> l.endsWith(".class"))
                .map(filename -> {
                    try {
                        return Class.forName(packageName + "." + filename.replaceAll("\\.class$", ""));
                    } catch (final ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(loadedClass -> this.addClass(loadedClass, true));
    }

    public void addClassesFromCurrentPackage() {
        this.addClassesFromClassPackage(this.getCallerClass(Thread.currentThread().getStackTrace()));
    }

    private Class<?> getCallerClass(final StackTraceElement[] stack) {
        if (stack.length < 3) {
            throw new IllegalStateException("Caller class couldn't be determined.");
        }
        try {
            return Class.forName(stack[2].getClassName());
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Caller class couldn't be found.");
        }
    }
}
