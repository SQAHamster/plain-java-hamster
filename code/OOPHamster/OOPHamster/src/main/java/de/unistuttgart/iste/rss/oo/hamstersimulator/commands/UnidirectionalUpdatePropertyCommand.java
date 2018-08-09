package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;
import javafx.beans.value.WritableListValue;
import javafx.beans.value.WritableSetValue;

public class UnidirectionalUpdatePropertyCommand<T> extends EntityCommand<T> {

    private Object oldValue;

    public UnidirectionalUpdatePropertyCommand(final PropertyMap<T> entity, final PropertyCommandSpecification spec) {
        super(entity, spec);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() {
        final Property<Object> property = this.entityState.getProperty(getSpecification().getPropertyName());
        switch (getSpecification().getActionKind()) {
        case SET:
            this.oldValue = property.getValue();
            property.setValue(getSpecification().getNewValue());
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(getSpecification().getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(getSpecification().getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(getSpecification().getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(getSpecification().getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void undo() {
        final Property<Object> property = this.entityState.getProperty(getSpecification().getPropertyName());
        switch (getSpecification().getActionKind()) {
        case SET:
            property.setValue(this.oldValue);
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(getSpecification().getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(getSpecification().getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(getSpecification().getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(getSpecification().getNewValue());
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
