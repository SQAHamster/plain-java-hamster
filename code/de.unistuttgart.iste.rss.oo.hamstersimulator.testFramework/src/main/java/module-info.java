module rss.hamster.testFramework {
    requires transitive rss.hamster.main;

    requires org.junit.jupiter.engine;
    requires org.junit.platform.launcher;
    requires org.junit.jupiter.api;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;
}