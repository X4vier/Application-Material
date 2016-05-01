package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by X4 on 9/22/2015.
 */
public class MovePieceTest {
    private static String[] games = {
            "171178187194205215" +"093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" +"060",
            "171178187194205215" +"093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" +"180",
            "171178187194205215" +"093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" +"201",
            "171178187194205215" +"093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" +"082"+"202",

    };

    @Test
    public void testCollisionWithWall() {
        HexGame hexGame = new HexGame(games[0]);
        assertTrue("Moved piece to wrong location, expected 216 but got " +hexGame.movePiece(0,Direction.UPLEFT), hexGame.movePiece(0, Direction.UPLEFT)==216);
    }

    @Test
    public void testCollisionWithNook() {
        HexGame hexGame = new HexGame(games[1]);
        assertTrue("Moved piece to wrong location, expected 64 but got " +hexGame.movePiece(0,Direction.LEFT), hexGame.movePiece(0, Direction.LEFT)==64);
    }

    @Test
    public void testCollisionWithCranny() {
        HexGame hexGame = new HexGame(games[2]);
        assertTrue("Moved piece to wrong location, expected 195 but got " +hexGame.movePiece(0,Direction.RIGHT), hexGame.movePiece(0, Direction.RIGHT)==195);
    }

    @Test
    public void testCollisionWithOtherPiece() {
        HexGame hexGame = new HexGame(games[3]);
        assertTrue("Moved piece to wrong location, expected 116 but got " +hexGame.movePiece(1,Direction.UPRIGHT), hexGame.movePiece(1, Direction.UPRIGHT)==116);
    }


}

