package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;

public class PropertyMap <T> {
    private final Map<String, Property<?>> editableProperties;
    private final T propertyOwner;

    public PropertyMap(final T propertyOwner, final Property<?> ... properties) {
        super();
        final HashMap<String, Property<?>> propertyMap = Maps.newHashMap();
        for (final Property<?> property : properties) {
            propertyMap.put(property.getName(), property);
        }
        this.editableProperties = ImmutableMap.copyOf(propertyMap);
        this.propertyOwner = propertyOwner;
    }

    public T getPropertyOwner() {
        return propertyOwner;
    }

    @SuppressWarnings("unchecked")
    public <G> Property<G> getProperty(final String name) {
        assert editableProperties.containsKey(name);

        return (Property<G>) editableProperties.get(name);
    }

    @SuppressWarnings("unchecked")
    public <G> ObjectProperty<G> getObjectProperty(final String name) {
        assert editableProperties.containsKey(name);

        return (ObjectProperty<G>) editableProperties.get(name);
    }

    @SuppressWarnings("unchecked")
    public <G> ListProperty<G> getListProperty(final String name) {
        assert editableProperties.containsKey(name);

        return (ListProperty<G>) editableProperties.get(name);
    }

}