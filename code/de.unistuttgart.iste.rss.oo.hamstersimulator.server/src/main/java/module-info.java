module rss.hamster.server {
    requires rss.hamster.core;
    requires rss.util;
    requires com.google.gson;
    requires javafx.base;
    requires jdk.httpserver;

    opens de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes to com.google.gson;
    opens de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta to com.google.gson;
    opens de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type to com.google.gson;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.server.input;
}