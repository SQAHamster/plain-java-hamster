package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import javafx.beans.property.ReadOnlyListProperty;

/**
 * Interface which provides read-only access to the log entries
 * It is not possible to create new log entries using this interface
 */
public interface ObservableLog {
    /**
     * Getter for the log property, which contains read-only log entries
     * @return the property (not null)
     */
    ReadOnlyListProperty<? extends ObservableLogEntry> logProperty();
}
