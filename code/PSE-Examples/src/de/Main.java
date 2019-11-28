package de;

import java.io.File;

public class Main {

    private static final String ERROR_LOG_FILENAME = "error.log";
    private static final String WARNING_LOG_FILENAME = "warning.log";
    private static final int DEFAULT_LOG_LEVEL = 0;
    private static final String DEBUG_LOG_FILENAME = "debug.log";

    public static void main(String[] args) {
        checkForMacOS();
        setupLogging();
    }

    private static void setupLogging() {
        // (2) do all the logfile setup stuff
        int currentLoggingLevel = DEFAULT_LOG_LEVEL;
        File errorFile = new File(ERROR_LOG_FILENAME);
        File warningFile = new File(WARNING_LOG_FILENAME);
        File debugFile = new File(DEBUG_LOG_FILENAME);
    }

    private static void checkForMacOS() {
        // (1) make sure the code only runs on mac os x

        if (!isMacOS()) {
            killJVM();
        }
    }

    private static void killJVM() {
        System.err.println("Not running on a Mac OS X system.");
        System.exit(1);
    }

    private static boolean isMacOS() {
        boolean mrjVersionExists = isRunningOnMacOSJVM();
        boolean osNameExists = System.getProperty("os.name").startsWith("Mac OS");
        return mrjVersionExists && osNameExists;
    }

    private static boolean isRunningOnMacOSJVM() {
        return System.getProperty("mrj.version") != null;
    }
}
