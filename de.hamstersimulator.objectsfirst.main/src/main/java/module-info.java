module sqa.hamster.main {
    requires transitive sqa.hamster.core;
    requires java.logging;
    requires transitive sqa.hamster.ui;
    requires sqa.hamster.config;
    requires sqa.hamster.server;
    requires com.google.gson;

    exports de.hamstersimulator.objectsfirst.external.model;
}
