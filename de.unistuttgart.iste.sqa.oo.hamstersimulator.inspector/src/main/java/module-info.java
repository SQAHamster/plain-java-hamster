module sqa.hamster.inspector {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires sqa.hamster.core;
    requires sqa.hamster.ui;
    requires sqa.hamster.main;
    requires org.controlsfx.controls;

    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui to javafx.fxml;

    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.model;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata;
}
