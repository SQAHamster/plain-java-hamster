package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;

public class ClassInstanceManager {

    private final InspectionViewModel viewModel;

    ClassInstanceManager(InspectionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public <T> void addClass(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class to be added can't be null");
        }
        Class<?> caller = this.getCallerClass(Thread.currentThread().getStackTrace());
        addClass(cls, caller.getPackage().equals(caller.getClass().getPackage()));
    }

    public <T> void addClass(Class<T> cls, boolean setAccessible) {
        if (!this.viewModel.hasViewModelForClass(cls)) {
            this.viewModel.classFactory.viewModelForClass(cls, setAccessible, setAccessible);
        } else {
            //TODO: Make already existing viewmodel accessible
        }
    }

    public void addInstance(Object instance, String name) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance to be added can't be null");
        }
        Class<?> caller = this.getCallerClass(Thread.currentThread().getStackTrace());
        addInstance(instance, name, caller.getPackage().equals(instance.getClass().getPackage()));
    }

    public void addInstance(Object instance, String name, boolean setAccessible) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance to be added can't be null");
        }
        Optional<InstanceViewModel> instanceViewModel = this.viewModel.getViewModelForObject(instance);
        if (instanceViewModel.isEmpty()) {
            this.viewModel.instanceFactory.createInstanceViewModel(instance, name, setAccessible);
        } else {
            //TODO: Make already existing viewmodel accessible
            instanceViewModel.get().nameProperty().set(name);
        }
    }

    public void addClassesFromClassPackage(Class<?> cls) {
        ClassLoader loader = cls.getClassLoader();
        String packageName = cls.getPackageName();
        InputStream classesInPackage = loader.getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader classNameReader = new BufferedReader(new InputStreamReader(classesInPackage));
        classNameReader.lines()
                .filter(l -> l.endsWith(".class"))
                .map(filename -> {
                    try {
                        return Class.forName(packageName + "." + filename.replaceAll("\\.class$", ""));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(loadedClass -> this.addClass(loadedClass, true));
    }

    public void addClassesFromCurrentPackage() {
        this.addClassesFromClassPackage(this.getCallerClass(Thread.currentThread().getStackTrace()));
    }

    private Class<?> getCallerClass(StackTraceElement[] stack) {
        if (stack.length < 3) {
            throw new IllegalStateException("Caller class couldn't be determined.");
        }
        try {
            return Class.forName(stack[2].getClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Caller class couldn't be found.");
        }
    }
}
