package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

public abstract class HamsterEvent {
    private final long time;

    public HamsterEvent(final long time) {
        this.time = time;
    }

    public final long getTime() {
        return this.time;
    }
}
