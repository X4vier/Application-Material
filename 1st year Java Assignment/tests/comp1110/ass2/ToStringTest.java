package comp1110.ass2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by steveb on 6/08/2015.
 */
public class ToStringTest {

    private static String[] games = {
            "171178187194205215" + "093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" + "093", // ok
            "171178187194205215" + "093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" + "038", // ok
            "171178187194205215" + "093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" + "064", // ok
            "171178187194205215" + "093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" + "070", // ok
            "171178187194205215" + "093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D" + "093038064070", // ok
    };

    private static final int CRANNIES = 6;
    private static final int CRANNIE_CHARS = 3;
    private static final int NOOKS = 18;
    private static final int NOOK_CHARS = 4;
    private static final int PIECE_CHARS = 3;

    /* normalize the order of the elements of the game string */
    private String normalizeGameString(String in) {
        String rtn = null;
        int index = 0;
        List<String> substrings;

        /* normalize the crannies */
        substrings = new ArrayList<>();
        for (int i = 0; i < CRANNIES; i++) {
            substrings.add(in.substring(index, index+CRANNIE_CHARS));
            index += CRANNIE_CHARS;
        }
        Collections.sort(substrings);
        for (String ss : substrings) {
            rtn += ss;
        }

        /* normalize the nooks */
        substrings = new ArrayList<>();
        for (int i = 0; i < NOOKS; i++) {
            substrings.add(in.substring(index, index+NOOK_CHARS));
            index += NOOK_CHARS;
        }
        Collections.sort(substrings);
        for (String ss : substrings) {
            rtn += ss;
        }

        /* normalize the pieces */
        substrings = new ArrayList<>();
        int pieces = (in.length()-index)/PIECE_CHARS;
        for (int i = 0; i < pieces; i++) {
            substrings.add(in.substring(index, index+PIECE_CHARS));
            index += PIECE_CHARS;
        }
        Collections.sort(substrings);
        for (String ss : substrings) {
            rtn += ss;
        }

        return rtn;
    }

    @Test
    public void testToString() {
        for (int i = 0; i < 5; i++) {
            HexGame game = new HexGame(games[i]);
            String result = game.toString();
            assertTrue("Empty string returned", result != null);
            assertTrue("Bad string returned for game state\nGot: "+result+"\nExpected: "+games[i], normalizeGameString(result).equals(normalizeGameString(games[i])));
        }
    }
}
