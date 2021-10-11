module sqa.hamster.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires sqa.hamster.core;

    opens de.hamstersimulator.objectsfirst.ui.javafx to javafx.fxml;
    opens de.hamstersimulator.objectsfirst.ui.ressources.fxml;
    opens de.hamstersimulator.objectsfirst.ui.ressources.css;
    opens de.hamstersimulator.objectsfirst.ui.ressources.images;

    exports de.hamstersimulator.objectsfirst.ui.javafx;
}
