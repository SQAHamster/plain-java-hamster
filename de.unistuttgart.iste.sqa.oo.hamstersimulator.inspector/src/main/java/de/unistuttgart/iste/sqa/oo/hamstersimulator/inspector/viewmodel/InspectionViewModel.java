package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Instance;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.InstanceMethod;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model.Type;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

public class InspectionViewModel {

    private final SimpleMapProperty<String, Instance> variables = new SimpleMapProperty<>(this, "variables", FXCollections.observableMap(new HashMap<>()));
    private final SimpleListProperty<Type> types = new SimpleListProperty<>(this, "types", FXCollections.observableList(new ArrayList<>()));

    private HamsterGame game;

    public InspectionViewModel(HamsterGame game) {
        this.game = game;
        types.add(Type.typeForClass(game.getClass()));
        types.add(Type.typeForClass(SimpleHamsterGame.class));
        types.add(Type.typeForClass(Hamster.class));
        variables.put("game", Instance.instanceForObject(game));
        variables.put("test", Instance.instanceForObject(15));
        variables.put("hamster", Instance.instanceForObject(new B()));
        variables.put("hamster2", Instance.instanceForObject(new C()));

    }

    public MapProperty<String, Instance> variablesProperty() {
        return this.variables;
    }

    public ListProperty<Type> typesProperty() {
        return this.types;
    }
}