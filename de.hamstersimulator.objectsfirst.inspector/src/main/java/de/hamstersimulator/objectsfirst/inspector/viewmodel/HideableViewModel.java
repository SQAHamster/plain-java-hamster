package de.hamstersimulator.objectsfirst.inspector.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class HideableViewModel extends NamedViewModel {
    private final SimpleBooleanProperty isVisible = new SimpleBooleanProperty(this, "isVisible");

    public HideableViewModel(String name) {
        super(name);
    }

    public BooleanProperty isVisibleProperty() {
        return this.isVisible;
    }
}
