package de.hamstersimulator.objectsfirst.inspector.ui;

/**
 * Result of an InputValidation
 */
public enum ValidationResult {
    /***
     * The input is valid and can be saved
     */
    OK,
    /**
     * The input is invalid and cannot be saved
     * Example: float when integer was expected
     */
    ERROR,
    /**
     * The input is valid and can be saved, however the type of input is highly discouraged to use
     * Example: null value for String
     */
    WARNING
}
