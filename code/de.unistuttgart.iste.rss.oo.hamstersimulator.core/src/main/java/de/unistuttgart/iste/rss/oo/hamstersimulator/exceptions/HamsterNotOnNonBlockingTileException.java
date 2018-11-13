/**
 * 
 */
package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

/**
 * @author Steffen Becker
 *
 */
public class HamsterNotOnNonBlockingTileException extends HamsterException {

    /**
     * 
     */
    private static final long serialVersionUID = -8722374886204626675L;

    /**
     * Constructor of the exception
     */
    public HamsterNotOnNonBlockingTileException() {
        super("You tried to move or create a hamster on a non-free tile.");
    }

}
