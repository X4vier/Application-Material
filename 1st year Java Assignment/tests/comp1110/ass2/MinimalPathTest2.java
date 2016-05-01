package comp1110.ass2;

import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertTrue;

/**
 * Created by X4 on 9/22/2015.
 */
public class MinimalPathTest2 {
    private static final int TRIALS = 100; // How many times we want the test to repeat (each time generates a different scenario)

    /**
     * This test works by randomly generating a HexGameGUI containing one piece, randomly selecting a nook, calling minimalPath()
     * to find the path from the piece to the selected nook, and then testing whether this path actually does take the piece to the
     * right nook. At the same time, it checks that each step taken along the path shrinks the length of the minimalPath by 1, and that
     * there is never a move which deviates from the minimalPath which shortens the journey by more than one move (or else our 'shortest path'
     * would be wrong)
     */

    @Test
    public void testWorkingPath() {
        for (int i=0; i<TRIALS; i++) {
            // Printing what trial we're up to to help with debugging:
            System.out.println("__________________________________________");
            System.out.println("TRIAL "+i);
            System.out.println("__________________________________________");

            // Randomly generated games have between 1 and 4 pieces, the code below ensures the game we work with has just one piece
            HexGame dummyGame = new HexGame();
            String gameString = dummyGame.toString().substring(0,93);
            HexGame hexGame = new HexGame(gameString);

            System.out.println("Game: "+hexGame.toString()); // To help with debugging

            Random r = new Random();
            int targetNook = r.nextInt(18); // Randomly chose one of the nooks in the game which the piece has to move to
            int goal = hexGame.getNooks()[targetNook].getPosition();

            System.out.println("Target nook: "+targetNook);
            int[] path = HexGame.minimalPath(hexGame.toString(), hexGame.getPieces()[0].getPosition(), goal);
            int lengthOfPath = path.length;

            // Print out the result of the minimalPath() call to help with debugging:
            System.out.println("length of path: " + lengthOfPath);
            System.out.println("Path: ");
            for (int j = 0; j<lengthOfPath; j++) {
                System.out.println(path[j]);
            }
            System.out.println();


            for (int j = 1; j<lengthOfPath; j++) {
                int nextPosition = path[j];
                boolean foundValidMove = false;

                for (Direction d : Direction.allDirections) {
                    int newPosition = SimulateMove.simulateMove(hexGame, 0, d);

                    // Make sure there's no move which shortens the minimalPath by more than 1 step
                    assertTrue("Minimal path not correct, moving in direction "+d+" to position "+newPosition+" on move "+j+" gives a shorter path",
                            HexGame.minimalPath(hexGame.toString(),newPosition,goal).length >= lengthOfPath-j);

                    // Make sure there's a move we can make which moves the piece along the path
                    if (SimulateMove.simulateMove(hexGame, 0,d)== nextPosition) {
                        System.out.println("Move "+j+" succeeded");
                        foundValidMove = true;
                        hexGame.movePiece(0, d);

                        // Make sure that if we make the right move and call minimalPath() again, we see the new path is one move shorter
                        assertTrue("At move "+j+"moving one step along the path to position "+newPosition+", then recalculating minimal path, did not yeild a new path which was one move shorter",
                                HexGame.minimalPath(hexGame.toString(), newPosition, goal).length == lengthOfPath-j);
                        break;
                    }
                }
                assertTrue("No valid move was found to take piece from "+hexGame.getPieces()[0].getPosition()+" to "+nextPosition ,foundValidMove);
            }
            System.out.println("Path works!");

        }
    }

}
