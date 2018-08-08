package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;
import javafx.beans.value.WritableListValue;
import javafx.beans.value.WritableSetValue;

public class UnidirectionalUpdatePropertyCommand<T> extends EntityCommand<T> {

    protected final PropertyCommandSpecification spec;
    private Object oldValue;

    public UnidirectionalUpdatePropertyCommand(final PropertyMap<T> entity, final PropertyCommandSpecification spec) {
        super(entity);
        this.spec = spec;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void execute() {
        final Property<Object> property = this.entityState.getProperty(this.spec.getPropertyName());
        switch (spec.getActionKind()) {
        case SET:
            this.oldValue = property.getValue();
            property.setValue(this.spec.getNewValue());
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(this.spec.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(this.spec.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(this.spec.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(this.spec.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void undo() {
        final Property<Object> property = this.entityState.getProperty(this.spec.getPropertyName());
        switch (spec.getActionKind()) {
        case SET:
            property.setValue(this.oldValue);
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(this.spec.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(this.spec.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(this.spec.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(this.spec.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        }
    }

    public static <G> UnidirectionalUpdatePropertyCommand<G> createPropertyUpdateCommand(
            final PropertyMap<G> entity,
            final String propertyName,
            final Object value,
            final ActionKind action) {
        return new UnidirectionalUpdatePropertyCommand<G>(entity, new PropertyCommandSpecification(propertyName, value, action));
    }
}
