package comp1110.ass2;

/**
 * Created by X4 on 8/22/2015.
 */
public class Nook {
    int position;
    Direction direction;

    public int getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Nook(String position, Character directionLetter) {
        switch (directionLetter) {
            case ('A'): this.direction = Direction.DOWNRIGHT;
                break;
            case ('B'): this.direction = Direction.DOWNLEFT;
                break;
            case ('C'): this.direction = Direction.LEFT;
                break;
            case ('D'): this.direction = Direction.UPLEFT;
                break;
            case ('E'): this.direction = Direction.UPRIGHT;
                break;
            case ('F'): this.direction = Direction.RIGHT;
                break;
        }
        this.position = Integer.parseInt(position);
    }

    @Override
    public String toString() {
        String s = "" + position + direction.toString();
        while (s.length()<4) {
            s = "0" + s;
        }
        return s;
    }
}
