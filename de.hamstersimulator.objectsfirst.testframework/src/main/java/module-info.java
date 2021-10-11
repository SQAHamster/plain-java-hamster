module sqa.hamster.testframework {
    requires transitive sqa.hamster.main;

    requires org.junit.jupiter.engine;
    requires org.junit.platform.launcher;
    requires transitive org.junit.jupiter.api;
    requires sqa.hamster.core;
    requires javafx.base;

    exports de.hamstersimulator.objectsfirst.testframework;
    exports de.hamstersimulator.objectsfirst.testframework.gamestate;
    exports de.hamstersimulator.objectsfirst.testframework.ltl;
}
