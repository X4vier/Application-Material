package comp1110.ass2;

/**
 * Created by X4 on 8/22/2015.
 */
public class Cranny {
    int position;

    public int getPosition() {
        return position;
    }

    public Cranny(String position) {
        this.position = Integer.parseInt(position);
    }

    @Override
    public String toString() {
        String s =  "" + position;
        while (s.length()<3) { // If the position is less than 100 we add preceding zeroes
            s = "0" + s;
        }
        return s;
    }
}
