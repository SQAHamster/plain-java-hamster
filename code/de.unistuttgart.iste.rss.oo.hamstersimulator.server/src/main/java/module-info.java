module rss.hamster.server {
    requires rss.hamster.core;
    requires rss.util;
    requires com.google.gson;
    requires io.javalin;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client;
}