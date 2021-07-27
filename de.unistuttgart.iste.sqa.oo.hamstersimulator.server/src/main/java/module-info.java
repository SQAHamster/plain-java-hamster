module sqa.hamster.server {
    requires sqa.hamster.core;
    requires sqa.util;
    requires com.google.gson;
    requires javafx.base;
    requires jdk.httpserver;

    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes to com.google.gson;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.delta to com.google.gson;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.type to com.google.gson;
    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.server.input to com.google.gson;

    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.server.http.client;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.server.input;
}
