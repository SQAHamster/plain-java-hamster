package de.unistuttgart.iste.rss.utils;

import java.util.Objects;

/**
 * A utility class to implement precondition checks in Java.
 * @author Steffen Becker
 *
 */
public final class Preconditions {

    public static void checkNotNull(final Object o, final String message) {
        Objects.requireNonNull(o, message);
    }

    public static void checkNotNull(final Object o) {
        Objects.requireNonNull(o);
    }

    public static void checkArgument(final boolean check, final String message) {
        if (!check) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgument(final boolean check) {
        if (!check) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkState(final boolean check, final String message) {
        if (!check) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkState(final boolean check) {
        if (!check) {
            throw new IllegalStateException();
        }
    }

    private Preconditions() {
    }

}
