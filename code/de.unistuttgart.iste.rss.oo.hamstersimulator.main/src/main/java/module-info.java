module rss.hamster.main {
    requires transitive rss.hamster.core;
    requires java.logging;
    requires transitive rss.hamster.ui;
    requires rss.hamster.config;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;
}