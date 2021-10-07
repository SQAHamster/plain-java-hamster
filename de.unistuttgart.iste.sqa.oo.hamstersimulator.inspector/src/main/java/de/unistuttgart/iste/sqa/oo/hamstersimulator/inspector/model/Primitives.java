package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;

import java.util.Optional;

public enum Primitives {
    INTEGER,
    LONG,
    SHORT,
    CHAR,
    BYTE,
    DOUBLE,
    FLOAT,
    STRING,
    BOOLEAN,
    ENUM,
    OPTIONAL,
    COMPLEX;
    
    static Primitives getForClass(Class<?> cls) {
        if (cls.isEnum()) {
            return Primitives.ENUM;
        } else if (Integer.class.equals(cls)) {
            return Primitives.INTEGER;
        } else if (Long.class.equals(cls)) {
            return Primitives.LONG;
        } else if (Short.class.equals(cls)) {
            return Primitives.SHORT;
        } else if (Byte.class.equals(cls)) {
            return Primitives.BYTE;
        } else if (Character.class.equals(cls)) {
            return Primitives.CHAR;
        } else if (Double.class.equals(cls)) {
            return Primitives.DOUBLE;
        } else if (Float.class.equals(cls)) {
            return Primitives.FLOAT;
        } else if (String.class.equals(cls)) {
            return Primitives.STRING;
        } else if (Boolean.class.equals(cls)) {
            return Primitives.BOOLEAN;
        } else if (Optional.class.equals(cls)) {
            return Primitives.OPTIONAL;
        } else {
            return Primitives.COMPLEX;
        }
    }
}
