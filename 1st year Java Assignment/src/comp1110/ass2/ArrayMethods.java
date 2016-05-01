package comp1110.ass2;

/**
 * Created by X4 on 8/25/2015.
 */
public class ArrayMethods {

    public static boolean contains(int[] array, int number) { // Determine whether an array of ints contains a particular number
        boolean a = false;
        for (int i : array) {
            if (i == number) {
                a = true;
            }
        }
        return a;
    }

    public static int maximum(int[] list) { // Return the largest number from an array of ints
        int max = list[0];
        for (int i = 0; i < list.length; i++) {
            max = Math.max(max, list[i]);
        }
        return max;
    }

}
