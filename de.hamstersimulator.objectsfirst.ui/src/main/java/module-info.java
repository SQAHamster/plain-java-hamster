module de.hamstersimulator.objectsfirst.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires de.hamstersimulator.objectsfirst.core;
    requires java.logging;

    opens de.hamstersimulator.objectsfirst.ui.javafx to javafx.fxml;
    opens de.hamstersimulator.objectsfirst.ui.ressources.fxml;
    opens de.hamstersimulator.objectsfirst.ui.ressources.css;
    opens de.hamstersimulator.objectsfirst.ui.ressources.images;

    exports de.hamstersimulator.objectsfirst.ui.javafx;
}
