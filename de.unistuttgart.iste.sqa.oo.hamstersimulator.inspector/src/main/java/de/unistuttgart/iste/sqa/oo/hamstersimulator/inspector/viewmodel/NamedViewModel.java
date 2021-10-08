package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NamedViewModel {
    private final SimpleStringProperty name;

    public NamedViewModel(final String name) {
        this.name = new SimpleStringProperty(this, "name", name);
    }

    public StringProperty nameProperty() {
        return this.name;
    }
}
