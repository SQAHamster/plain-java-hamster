/**
 * @author Max Maier
 * @version 1.0
 * 
 * A class which forms the basis for a chess game.
 * 
 * Chess is a game played by two players each having
 * 16 chessman at the beginning of the game.
 * 
 * Chesman of different kind exist which all can perform
 * different types of moves.
 * 
 * Objective of the game is to achieve a chess mate which 
 * is when the opposite's king cannot move anymore.
 * 
 * Chess is played on a field of 8x8 tiles.
 * 
 */
class Chess {

    /**
     * Moves the given chessman. 
     * 
     * Verifies whether the given location can be reached by a valid move.
     * If so, moves the chess man to its target. If the target is occupied by
     * another enemy chessman, this one is removed form the game.
     * 
     * @param man Deztermines the chess man to move
     * @param location The destination tile of the move
     * @throws InvalidMoveCommandedException Is raised if move is invalid
     */
    void move(Chessman man, Location location) {

    }
}