package comp1110.ass2;

import sun.misc.LRUCache;

/**
 * Created by X4 on 8/22/2015.
 */
public enum Direction {
    RIGHT(0),UPRIGHT(1),UPLEFT(2),LEFT(3),DOWNLEFT(4),DOWNRIGHT(5);

    public int value;

    Direction(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case DOWNRIGHT: return "A";
            case DOWNLEFT: return "B";
            case LEFT: return "C";
            case UPLEFT: return "D";
            case UPRIGHT: return "E";
            case RIGHT: return "F";
            default: return null;
        }
    }

    public String getName() {
        switch (this) {
            case DOWNRIGHT: return "DOWNRIGHT";
            case DOWNLEFT: return "DOWNLEFT";
            case LEFT: return "LEFT";
            case UPLEFT: return "UPLEFT";
            case UPRIGHT: return "UPRIGHT";
            case RIGHT: return "RIGHT";
            default: return null;
        }
    }

    public Direction[] adjacentDirections() { // Directions which are 'close' to a given direction (useful for nook collisions)
        switch (this) {
            case DOWNRIGHT: return new Direction[] {RIGHT,DOWNRIGHT,DOWNLEFT};
            case DOWNLEFT: return new Direction[] {DOWNRIGHT,DOWNLEFT, LEFT};
            case LEFT: return new Direction[] {DOWNLEFT,LEFT,UPLEFT};
            case UPLEFT: return new Direction[] {LEFT,UPLEFT,UPRIGHT};
            case UPRIGHT: return new Direction[] {UPLEFT,UPRIGHT,RIGHT};
            case RIGHT: return new Direction[] {UPRIGHT,RIGHT,DOWNRIGHT};
            default: return null;
        }
    }

    public Direction[] oppositeDirections() { // Directions which are NOT 'close' to a given direction (useful for nook collisions)
        switch (this) {
            case DOWNRIGHT: return new Direction[] {LEFT,UPLEFT,UPRIGHT};
            case DOWNLEFT: return new Direction[] {UPLEFT,UPRIGHT,RIGHT};
            case LEFT: return new Direction[] {UPRIGHT,RIGHT,DOWNRIGHT};
            case UPLEFT: return new Direction[] {RIGHT,DOWNRIGHT,DOWNLEFT};
            case UPRIGHT: return new Direction[] {DOWNRIGHT,DOWNLEFT, LEFT};
            case RIGHT: return new Direction[] {DOWNLEFT,LEFT,UPLEFT};
            default: return null;
        }
    }

    public static char convertIntToChar(int number) {
            switch (number) {
                case 0:
                    return 'F';
                case 1:
                    return 'A';
                case 2:
                    return 'B';
                case 3:
                    return 'C';
                case 4:
                    return 'D';
                case 5:
                    return 'E';
                default:
                    return'!';
            }
        }

    public static Direction[] allDirections = {Direction.RIGHT,Direction.UPRIGHT,Direction.UPLEFT, Direction.LEFT,Direction.DOWNLEFT,Direction.DOWNRIGHT}; // For when we want to loop through the directions
}
