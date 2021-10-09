package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.util.Optional;

public class Type {

    public static final Type OBJECT_TYPE = new Type(Object.class);

    private final TypeCategory category;
    private final Class<?> type;

    public Type(final Class<?> cls) {
        this.type = cls;
        this.category = TypeCategory.getFromClass(cls);
    }

    public TypeCategory getCategory() {
        return this.category;
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String toString() {
        if (this.category != TypeCategory.ENUM) {
            return this.category.toString();
        } else {
            return "Enum: " + this.type.getSimpleName();
        }
    }
}
