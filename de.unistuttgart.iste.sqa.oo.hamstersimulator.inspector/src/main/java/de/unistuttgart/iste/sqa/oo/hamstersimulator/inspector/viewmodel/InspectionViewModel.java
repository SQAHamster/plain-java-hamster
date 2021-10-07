package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Instance;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.InstanceMethod;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

public class InspectionViewModel {

    private Map<String, Instance> variables = new HashMap<>();
    private List<Type> types = new ArrayList<>();

    private HamsterGame game;

    public InspectionViewModel(HamsterGame game) {
        this.game = game;
        types.add(Type.typeForClass(game.getClass()));
        variables.put("game", new Instance(game, Type.typeForClass(game.getClass())));
        variables.put("test", new Instance(15, Type.typeForClass(Integer.class)));
    }

    public ObservableMap<String, Instance> getVariables() {
        return new ObservableMapWrapper<>(Collections.unmodifiableMap(this.variables));
    }

    public ObservableList<Type> getTypes() {
        return new ObservableListWrapper<>(Collections.unmodifiableList(types));
    }
}
