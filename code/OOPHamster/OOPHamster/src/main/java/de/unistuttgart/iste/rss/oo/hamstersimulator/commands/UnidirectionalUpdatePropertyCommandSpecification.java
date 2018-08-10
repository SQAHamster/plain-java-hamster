package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public final class UnidirectionalUpdatePropertyCommandSpecification  {

    public enum ActionKind {
        SET, ADD, REMOVE
    }

    private final Object newValue;
    private final ActionKind actionKind;

    public UnidirectionalUpdatePropertyCommandSpecification(final Object newValue, final ActionKind actionKind) {
        super();
        this.newValue = newValue;
        this.actionKind = actionKind;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }

    public Object getNewValue() {
        return newValue;
    }

}
