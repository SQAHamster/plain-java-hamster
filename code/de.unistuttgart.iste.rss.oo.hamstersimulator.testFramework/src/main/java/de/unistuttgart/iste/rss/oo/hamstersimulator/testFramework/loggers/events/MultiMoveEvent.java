package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.loggers.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;

public class MultiMoveEvent extends HamsterEvent {

    private final ObservableTile before;
    private final ObservableTile after;

    public MultiMoveEvent(final long time, final ObservableTile before, final ObservableTile after) {
        super(time);
        this.before = before;
        this.after = after;
    }

    protected int getDeltaX() {
        return this.after.getLocation().getColumn() - this.before.getLocation().getColumn();
    }

    protected int getDeltaY() {
        return this.after.getLocation().getRow() - this.before.getLocation().getRow();
    }

    public int getSteps() {
        return Math.abs(this.getDeltaX()) + Math.abs(this.getDeltaY());
    }

    public ObservableTile getBefore() {
        return this.before;
    }

    public ObservableTile getAfter() {
        return this.after;
    }
}
