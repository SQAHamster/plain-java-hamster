/**
 *
 */
package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;

import java.util.Optional;

/**
 * Dummy IO interface for the hamster game. This IO interface is the default.
 * It basically does not support reading value and on exceptions it just
 * rethrows them.
 *
 * @author Steffen Becker
 *
 */
public class DummyInputInterface implements InputInterface {

    /* (non-Javadoc)
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface#readInteger(java.lang.String)
     */
    @Override
    public Optional<Integer> readInteger(final String message) {
        throw new UnsupportedOperationException("Dummy IO interface does not support reading values");
    }

    /* (non-Javadoc)
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface#readString(java.lang.String)
     */
    @Override
    public Optional<String> readString(final String message) {
        throw new UnsupportedOperationException("Dummy IO interface does not support reading values");
    }

    /* (non-Javadoc)
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface#showAlert(java.lang.Throwable)
     */
    @Override
    public void showAlert(final Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        throw new RuntimeException(t);
    }

    @Override
    public void abort() {
        //non of the methods is blocking, so do nothing
    }

}