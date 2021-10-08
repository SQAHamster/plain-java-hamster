package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Instance;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;

import java.lang.ref.WeakReference;

public class InstanceFactory {

    private InspectionViewModel viewModel;

    public InstanceFactory(InspectionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public Instance instanceForObject(Object obj) {
        if (obj == null) {
            return Instance.NULL_INSTANCE;
        }
        WeakReference<Instance> instanceRef = Instance.knownObjects.get(obj);
        Instance i = (instanceRef == null ? null : instanceRef.get());
        if (i == null) {
            Type t = Type.typeForClass(obj.getClass());
            i = new Instance(obj, t);
            instanceRef = new WeakReference<>(i);
            this.viewModel.allInstancesProperty().removeIf(ref -> ref.get() == null);
            this.viewModel.allInstancesProperty().add(instanceRef);
            Instance.knownObjects.put(obj, instanceRef);
        }
        return i;
    }
}
