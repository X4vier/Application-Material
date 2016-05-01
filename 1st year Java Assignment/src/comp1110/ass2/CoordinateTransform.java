package comp1110.ass2;

/**
 * Created by X4 on 8/15/2015.
 */

/**
 * The purpose of this class is to transform  between two coordinate systems:
 * the single number coordinate system given in the assitnment specification, and
 * a 2-dimensional coordinate system I designed where the origin is at position 0
 * ( the center of the board), the X-axis is horizontal pointing in the same direction as
 * a vector from position 0 to position 185,and the Y-axis is slighlty tilted away from
 * the vertical, pointing in the same direction as a vector from positon 0 to position
 * 216
 */

public class CoordinateTransform {

    /**
     * The board can be imagined as being made up of a series of concentric hexagonal rings,
     * with each ring corresponding to a different 'layer'. e.g 0 is on layer 1, 3 is on layer 2,
     * 10 is on layer 3, etc. The getLayer method takes a position on the board and returns what
     * layer that position is on.
     */

    public static int getLayer(int position) {
        int layer = 1;
        while (1+3*layer*(layer-1) <= position) {
            layer++;
        }
        return layer;
    }

    /**
     * The getXYCoords method transfors from the single-number coordinate system
     * in the assignment specification, to the XY-coordinate system described in
     * comment above.
     */

    public static XYcoord getXYCoords(int position) {
        if (position==0) {
            return new XYcoord(0,0);
        }
        int x;
        int y;
        int layer = getLayer(position);

        int indexInLayer = position - (1+3*(layer-1)*(layer-2));
        if (indexInLayer < layer) {
            y = layer-1;
        }
        else if(indexInLayer < layer*3 -3) {
            y = 2*layer -indexInLayer -2;
        }
        else if (indexInLayer < layer*4 -3 ) {
            y = 1 - layer;
        }
        else if (indexInLayer < layer*6 - 6) {
            y = (5 + indexInLayer - 5*layer);
        }
        else {
            y = 420;  // This should never happen
        }

        if (indexInLayer < layer) {
            x = indexInLayer;
        }
        else if (indexInLayer < layer*2 - 1) {
            x = layer-1;
        }
        else if (indexInLayer < layer*4 - 3) {
            x = 3*layer - 3 - indexInLayer;
        }
        else if (indexInLayer < layer*5 - 4) {
            x = 1-layer;
        }
        else if (indexInLayer < layer*6-6) {
            x = -layer + (indexInLayer - (layer-1)*5)+1;
        }
        else {
            x = 420;  // This should never happen
        }
        return new XYcoord(x,y);
    }

    /**
     * The getPosition method transforms back from the XY-coordinate
     * system to the single number coordinates in the assignment specification.
     */

    public static int getPosition(XYcoord coordinate) {
        if (coordinate.x==0 && coordinate.y == 0) {
            return 0;
        }
        int x = coordinate.x;
        int y = coordinate.y;
        int[] radialDistances = {x,-x,y,-y,y-x,x-y};
        int layer = ArrayMethods.maximum(radialDistances);

        /*
         * Once we have narrowed the position down to a particular layer,
         * a feasible way of getting the exact position is trial-and-error
         * with the inverse of this method, getXYCoords.
         */

        int lowestIndexInLayer = 6*(layer)*(layer-1)/2 + 1;
        int highestIndexInLayer = 6*layer*(layer+1)/2;
        for (int i = lowestIndexInLayer; i<= highestIndexInLayer; i++) {
            if (getXYCoords(i).x == coordinate.x && getXYCoords(i).y == coordinate.y) {
                return i;
            }
        }
        return 420; // This should never happen
    }

}