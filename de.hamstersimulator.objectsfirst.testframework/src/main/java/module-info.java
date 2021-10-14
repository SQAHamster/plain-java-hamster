module de.hamstersimulator.objectsfirst.testframework {
    requires transitive de.hamstersimulator.objectsfirst.main;

    requires org.junit.jupiter.engine;
    requires org.junit.platform.launcher;
    requires transitive org.junit.jupiter.api;
    requires de.hamstersimulator.objectsfirst.core;
    requires javafx.base;

    exports de.hamstersimulator.objectsfirst.testframework;
    exports de.hamstersimulator.objectsfirst.testframework.gamestate;
    exports de.hamstersimulator.objectsfirst.testframework.ltl;
}
