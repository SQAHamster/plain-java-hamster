package de.hamstersimulator.objectsfirst.server.datatypes.type;

/**
 * Type of the Delta
 * Each delta should have a unique type
 */
public enum DeltaType {
    ADD_TILE_CONTENT,
    REMOVE_TILE_CONTENT,
    ROTATE_HAMSTER,
    ADD_LOG_ENTRY,
    REMOVE_LOG_ENTRY,
    NEW_TERRITORY,
}
