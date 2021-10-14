module de.hamstersimulator.objectsfirst.inspector {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires de.hamstersimulator.objectsfirst.core;
    requires de.hamstersimulator.objectsfirst.ui;
    requires de.hamstersimulator.objectsfirst.main;
    requires org.controlsfx.controls;

    opens de.hamstersimulator.objectsfirst.inspector.ui to javafx.fxml;

    exports de.hamstersimulator.objectsfirst.inspector.ui;
    exports de.hamstersimulator.objectsfirst.inspector.viewmodel;
    exports de.hamstersimulator.objectsfirst.inspector.model;
    exports de.hamstersimulator.objectsfirst.inspector.testdata;
}
