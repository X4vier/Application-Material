package comp1110.ass2;

/**
 * Created by X4 on 8/14/2015.
 */
public class Piece {
    int number;
    int position;

    public Piece (String position, int player) {
        this.position = Integer.parseInt(position);
        this.number = player;
    }

    public int getNumber() {
        return number;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        String s =  "" + position;
        while (s.length()<3) {
            s = "0" + s;
        }
        return s;
    }
}
