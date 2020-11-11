module rss.hamster.main {
    requires transitive rss.hamster.core;
    requires java.logging;
    requires transitive rss.hamster.ui;
    requires rss.hamster.config;
    requires rss.hamster.server;
    requires com.google.gson;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;
}