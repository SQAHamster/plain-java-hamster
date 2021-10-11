module de.hamstersimulator.objectsfirst.server {
    requires de.hamstersimulator.objectsfirst.core;
    requires de.hamstersimulator.objectsfirst.util;
    requires com.google.gson;
    requires javafx.base;
    requires jdk.httpserver;

    opens de.hamstersimulator.objectsfirst.server.datatypes to com.google.gson;
    opens de.hamstersimulator.objectsfirst.server.datatypes.delta to com.google.gson;
    opens de.hamstersimulator.objectsfirst.server.datatypes.type to com.google.gson;
    opens de.hamstersimulator.objectsfirst.server.input to com.google.gson;

    exports de.hamstersimulator.objectsfirst.server.http.client;
    exports de.hamstersimulator.objectsfirst.server.input;
}
