module rss.hamster.testFramework {
    requires transitive rss.hamster.main;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.platform.commons;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;
}