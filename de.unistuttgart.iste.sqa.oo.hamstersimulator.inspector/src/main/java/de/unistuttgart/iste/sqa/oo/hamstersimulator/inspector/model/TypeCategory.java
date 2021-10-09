package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.util.Optional;

public enum TypeCategory {
    BYTE,
    SHORT,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    CHARACTER,
    STRING,
    ENUM,
    OBJECT,
    OPTIONAL,
    COMPLEX;
    
    public static TypeCategory getFromClass(Class<?> cls) {
        if (cls == byte.class || cls == Byte.class) {
            return TypeCategory.BYTE;
        } else if (cls == short.class || cls == Short.class) {
            return TypeCategory.SHORT;
        } else if (cls == int.class || cls == Integer.class) {
            return TypeCategory.INTEGER;
        } else if (cls == long.class || cls == Long.class) {
            return TypeCategory.LONG;
        } else if (cls == float.class || cls == Float.class) {
            return TypeCategory.FLOAT;
        } else if (cls == double.class || cls == Double.class) {
            return TypeCategory.DOUBLE;
        } else if (cls == boolean.class || cls == Boolean.class) {
            return TypeCategory.BOOLEAN;
        } else if (cls == char.class || cls == Character.class) {
            return TypeCategory.CHARACTER;
        } else if (cls == String.class) {
            return TypeCategory.STRING;
        } else if (cls == Object.class) {
            return TypeCategory.OBJECT;
        } else if (cls == Optional.class) {
            return TypeCategory.OPTIONAL;
        } else if (cls.isEnum()) {
            return TypeCategory.ENUM;
        } else {
            return TypeCategory.COMPLEX;
        }
    }
}
