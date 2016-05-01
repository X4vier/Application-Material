package comp1110.ass2;

/**
 * Created by X4 on 8/24/2015.
 */

/**  The purpose of this class is to provide a version of the movePiece method
  *  which doesn't cause any side-effects.
  */
public class SimulateMove {

    public static int simulateMove(HexGame game, int piece, Direction direction) {
        HexGame dummyGame = new HexGame(game.toString());
        return dummyGame.movePiece(piece,direction);
    }
}
