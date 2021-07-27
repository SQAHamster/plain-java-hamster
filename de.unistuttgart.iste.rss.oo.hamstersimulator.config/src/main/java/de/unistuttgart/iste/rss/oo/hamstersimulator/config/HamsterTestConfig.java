package de.unistuttgart.iste.rss.oo.hamstersimulator.config;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the config needed for testing
 */
public class HamsterTestConfig {

    /**
     * The list containing all tests in the same order as the exercise classes in the {@link HamsterConfig}
     */
    private List<String> testClasses;

    /**
     * It's not allowed to create a new config object. A file must be loaded
     */
    private HamsterTestConfig() {}

    /**
     * Returns the list of test classes to be used to verify the exercises
     * @return An unmodifiable version of the list of class names
     */
    public List<String> getTestClasses() {
        return Collections.unmodifiableList(testClasses);
    }

    /**
     * Loads the test config from the default file: {@code config.tests.json}
     * @return A new instance of {@link HamsterTestConfig} with the data loaded from the JSON-file
     * @throws IOException If the loading of the JSON-file fails (e.g. because the file doesn't exist or isn't readable)
     */
    public static HamsterTestConfig load() throws IOException {
        return load("config.tests.json");
    }

    /**
     * Loads the test config from the specified JSON-file and returns a new instance
     *
     * @param path The absolute or relative path to the JSON-file to be loaded as test config
     *             This can't be null or empty
     * @return A new instance of {@link HamsterTestConfig} with the data loaded from the JSON-file
     * @throws IOException If the loading of the JSON-file fails (e.g. because the file doesn't exist or isn't readable)
     */
    public static HamsterTestConfig load(String path) throws IOException {
        if (path == null || path.length() <= 0) {
            throw new IllegalArgumentException("The path can't be null or empty");
        }
        Gson gson = new Gson();
        return gson.fromJson(Files.newBufferedReader(Path.of(path)), HamsterTestConfig.class);
    }
}
