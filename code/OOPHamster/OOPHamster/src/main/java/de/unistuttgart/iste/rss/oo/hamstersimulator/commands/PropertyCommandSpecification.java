package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public class PropertyCommandSpecification {

    public enum ActionKind {
        SET, ADD, REMOVE
    }

    private final String propertyName;
    private final Object newValue;
    private final ActionKind actionKind;

    public PropertyCommandSpecification(final String propertyName, final Object newValue, final ActionKind actionKind) {
        super();
        this.propertyName = propertyName;
        this.newValue = newValue;
        this.actionKind = actionKind;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getNewValue() {
        return newValue;
    }

}
