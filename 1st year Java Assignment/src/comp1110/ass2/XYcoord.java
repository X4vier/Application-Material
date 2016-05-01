package comp1110.ass2;

/**
 * Created by X4 on 8/23/2015.
 */



public class XYcoord {
    public int x;
    public int y;

    public XYcoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XYcoord(int position) {
        XYcoord a = CoordinateTransform.getXYCoords(position);
        this.x = a.x;
        this.y = a.y;
    }

    public int toPosition() {
        return CoordinateTransform.getPosition(this);
    }

    public String positionAsString() {
        String s = "" + toPosition();
        while (s.length()<3) {
            s = "0"+s;
        }
        return s;
    }

    @Override
    public String toString() {
        return "XYcoord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
