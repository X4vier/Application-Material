package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by apple on 23/09/2015.
 */
public class SupplyNookTest {
        private static String[] nooks = {
                "126A126B126C062A064B096C098A100F024E012A047D105F029A077A114C120D084E054D",  // 126 has 3 orientations
                "093A096B124A122C058F120D016C082E077A029B110C012D105A107F098A100A102A010B",  // too less from one tri
                "018A059B090c096D040F008E067A069F102B012C046E106C014D110A112C120E056F016C",  // 090c typing mistake
                "112052A029C012C046F106E024A068B102C065E021C062A124A087C036F120B056C032E",   // 112 has no orientation
        };

    @Test
    public void testMultipleOrientation() {
        assertFalse("Incorrectly accepted more than one orientation in the same nook: " + nooks[0], HexGame.legitimateNooks(nooks[0]));
    }

    @Test
    public void testTooLess() {
        assertFalse("Incorrectly accepted too few nooks from one triangle: " + nooks[1], HexGame.legitimateNooks(nooks[1]));
    }

    @Test
    public void testTypingMistake() {
        assertFalse("Incorrectly accepted typing mistake: " + nooks[2], HexGame.legitimateNooks(nooks[2]));
    }

    @Test
    public void testNoOrientation() {
        assertFalse("Incorrectly accepted nooks without orientation: " + nooks[3], HexGame.legitimateNooks(nooks[3]));
    }


}

