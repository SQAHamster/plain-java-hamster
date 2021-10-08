package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.util.Optional;

public class Type {

    public static final Type OBJECT_TYPE = new Type(Object.class);

    private final TypeCategory category;
    private final Class<?> type;

    public Type(final Class<?> type) {
        this.type = type;
        if (type == byte.class || type == Byte.class) {
            this.category = TypeCategory.BYTE;
        } else if (type == short.class || type == Short.class) {
            this.category = TypeCategory.SHORT;
        } else if (type == int.class || type == Integer.class) {
            this.category = TypeCategory.INTEGER;
        } else if (type == long.class || type == Long.class) {
            this.category = TypeCategory.LONG;
        } else if (type == float.class || type == Float.class) {
            this.category = TypeCategory.FLOAT;
        } else if (type == double.class || type == Double.class) {
            this.category = TypeCategory.DOUBLE;
        } else if (type == boolean.class || type == Boolean.class) {
            this.category = TypeCategory.BOOLEAN;
        } else if (type == char.class || type == Character.class) {
            this.category = TypeCategory.CHARACTER;
        } else if (type == String.class) {
            this.category = TypeCategory.STRING;
        } else if (type == Object.class) {
            this.category = TypeCategory.OBJECT;
        } else if (type == Optional.class) {
            this.category = TypeCategory.OPTIONAL;
        } else if (type.isEnum()) {
            this.category = TypeCategory.ENUM;
        } else {
            this.category = TypeCategory.COMPLEX;
        }
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
