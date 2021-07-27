module rss.hamster.testframework {
    requires transitive rss.hamster.main;

    requires org.junit.jupiter.engine;
    requires org.junit.platform.launcher;
    requires transitive org.junit.jupiter.api;
    requires rss.hamster.core;
    requires javafx.base;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;
}