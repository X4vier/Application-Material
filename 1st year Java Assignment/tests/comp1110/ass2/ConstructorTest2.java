package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by X4 on 9/24/2015.
 */
public class ConstructorTest2 {

    private static final int TRIALS = 200; // unfortunately since the HexGameGUI() constructor is a bit slow, this number can't be set that high

    @Test
    public void testCranniesUniformlyDistributed() {
        int[] crannyCount = new int[48]; // There are 48 positions a cranny can be in
        for (int i = 0; i<48; i++) {
            crannyCount[i] = 0;
        }

        for (int i = 0; i < TRIALS; i++) {
            HexGame hexGame = new HexGame();
            for (Cranny c : hexGame.getCrannies()) {
                crannyCount[c.getPosition()-169] ++;
            }
        }

        int expected = TRIALS/8;

        for (int i = 0; i<48; i++) {
            assertTrue("Abnormal number of crannies in position "+(i+169)+". Expected about "+expected+", got "+crannyCount[i],
                    crannyCount[i]>expected/2 && crannyCount[i]<expected*2);
        }

    }

    @Test
    public void testDirectionsUniformlyDistributed() {

        int[] directionCount = new int[6];
        for (int i = 0; i<6; i++) {
            directionCount[i] = 0;
        }

        for (int i = 0; i < TRIALS; i++) {
            HexGame hexGame = new HexGame();
            for (Nook n : hexGame.getNooks()) {
                directionCount[n.getDirection().value] ++;
            }
        }

        int expected = TRIALS*18/6;

        for (int i = 0; i<6; i++) {
            assertTrue("Abnormal number of nooks pointing in direction "+Direction.convertIntToChar(i)+". Expected about "+expected+", got "+directionCount[i],
                    directionCount[i]>expected/2 && directionCount[i]<expected*2);
        }

    }

}
