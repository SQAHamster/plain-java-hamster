package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import de.hamstersimulator.objectsfirst.inspector.model.Type;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MethodViewModel extends NamedViewModel {

    private final ReadOnlyListWrapper<ParamViewModel> params;
    private final ReadOnlyObjectWrapper<Type> returnType;
    private final Function<List<?>, ?> methodCallback;
    private final SimpleStringProperty methodSignature;

    public MethodViewModel(final String name, final List<ParamViewModel> params, final Type returnType, final Function<List<?>, ?> methodCallback) {
        super(name);
        this.params = new ReadOnlyListWrapper<>(this, "params", FXCollections.observableList(params));
        this.returnType = new ReadOnlyObjectWrapper<>(this, "returnType", returnType);
        this.methodCallback = methodCallback;
        this.methodSignature = new SimpleStringProperty(this, "methodSignature", name);
        this.updateMethodSignature(null);
        this.params.addListener(this::updateMethodSignature);
        this.returnType.addListener(this::updateMethodSignature);
    }

    public void updateMethodSignature(Observable observable) {
        this.methodSignature.set(
                this.returnType.get().toString()
                        + " "
                        + this.nameProperty().get()
                        + "("
                        + this.params.stream().map(vm -> vm.typeProperty().get().toString() + " " + vm.nameProperty().get()).collect(Collectors.joining(", "))
                        + ")"
        );
    }

    public ReadOnlyListProperty<ParamViewModel> paramsProperty() {
        return this.params.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Type> returnTypeProperty() {
        return this.returnType.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty methodSignatureProperty() {
        return this.methodSignature;
    }

    public Object call(final List<Object> values) {
        return this.methodCallback.apply(values);
    }
}
