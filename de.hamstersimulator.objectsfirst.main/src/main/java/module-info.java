module de.hamstersimulator.objectsfirst.main {
    requires transitive de.hamstersimulator.objectsfirst.core;
    requires java.logging;
    requires transitive de.hamstersimulator.objectsfirst.ui;
    requires de.hamstersimulator.objectsfirst.config;
    requires de.hamstersimulator.objectsfirst.server;
    requires com.google.gson;

    exports de.hamstersimulator.objectsfirst.external.model;
    exports de.hamstersimulator.objectsfirst.external.simple.game;
    opens de.hamstersimulator.objectsfirst.external.simple.game to de.hamstersimulator.objectsfirst.inspector;
}
