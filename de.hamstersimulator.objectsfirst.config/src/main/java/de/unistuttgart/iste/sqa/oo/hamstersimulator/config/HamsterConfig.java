package de.unistuttgart.iste.sqa.oo.hamstersimulator.config;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class HamsterConfig {

    /**
     * A list of classes containing the {@code public static void main(String[] args)}. One for each exercise
     */
    private List<String> exercises;

    /**
     * If set, the string name of the default output interface to use
     */
    private String output;


    private HamsterConfig() {

    }

    /**
     * Returns the list of class names o be used for the exercises. The order is important as it is used to map the exercises to the tests
     * @return An unmodifiable version of the list of exercise classes
     */
    public List<String> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    /**
     * Returns the string name of the default output interface to use
     * @return The string of the output interface to use. This can be null if it's not given in the JSON
     */
    public String getOutput() {
        return output;
    }

    /**
     * Loads the config from the default file: {@code config.json}
     * @return A new instance of {@link HamsterConfig} with the data loaded from the JSON-file
     * @throws IOException If the loading of the JSON-file fails (e.g. because the file doesn't exist or isn't readable)
     */
    public static HamsterConfig load() throws IOException {
        return load("config.json");
    }

    /**
     * Loads the config from the specified JSON-file and returns a new instance
     *
     * @param path The absolute or relative path to the JSON-file to be loaded as config
     *             This can't be null or empty
     * @return A new instance of {@link HamsterConfig} with the data loaded from the JSON-file
     * @throws IOException If the loading of the JSON-file fails (e.g. because the file doesn't exist or isn't readable)
     */
    public static HamsterConfig load(String path) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(Files.newBufferedReader(Path.of(path)), HamsterConfig.class);
    }
}
