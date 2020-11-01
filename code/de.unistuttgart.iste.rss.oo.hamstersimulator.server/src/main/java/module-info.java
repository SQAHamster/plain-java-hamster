module rss.hamster.server {
    requires rss.hamster.core;
    requires rss.util;
    requires com.google.gson;
    requires javafx.base;
    requires jdk.httpserver;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;
}