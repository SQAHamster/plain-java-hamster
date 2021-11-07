package de.hamstersimulator.objectsfirst.testframework.gamelog;

import de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes.GameLog;

/**
 * Can be used to generate an Exception which contains a GameLog and the message of another exception
 */
public class GameLogException extends RuntimeException {

    /***
     * Marker text used for exception message which contains
     */
    private static final String hamsterMarker = "##hamster##";

    public GameLogException(final Exception cause, final GameLog gameLog) {
        super(hamsterMarker + gameLog.toJson() + hamsterMarker + cause.getMessage(), cause);
    }
}
