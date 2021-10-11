module sqa.hamster.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires sqa.hamster.core;

    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx to javafx.fxml;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.ressources.fxml;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.ressources.css;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.ressources.images;

    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx;
}
