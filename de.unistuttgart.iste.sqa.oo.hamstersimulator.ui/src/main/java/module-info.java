module rss.hamster.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires rss.hamster.core;

    opens de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx to javafx.fxml;
    opens de.unistuttgart.iste.rss.oo.hamstersimulator.ui.ressources.fxml;
    opens de.unistuttgart.iste.rss.oo.hamstersimulator.ui.ressources.css;
    opens de.unistuttgart.iste.rss.oo.hamstersimulator.ui.ressources.images;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;
}