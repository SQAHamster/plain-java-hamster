package de.unistuttgart.iste.rss.oo.hamstersimulator.server.input;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.InputMessage;

public interface InputListener {
    void onInput(final InputMessage inputMessage);
}
