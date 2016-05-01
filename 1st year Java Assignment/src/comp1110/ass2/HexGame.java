package comp1110.ass2;

/**
 * Created by steveb on 30/07/2015.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class HexGame {

    Piece[] pieces;
    Nook[] nooks;
    Cranny[] crannies;

    public Piece[] getPieces() {
        return pieces;
    }

    public Nook[] getNooks() {
        return nooks;
    }

    public Cranny[] getCrannies() {
        return crannies;
    }

    /**
     * Construct HexGameGUI from a string describing game state.
     * @param game The initial state of the game, described as a string according to the assignment specification.
     */
    public HexGame(String game) {
        String cranniesString = game.substring(0,18);
        String nooksString = game.substring(18,90);
        String piecesString = game.substring(90);

        int numPieces = piecesString.length()/3;

        Piece[] pieces = new Piece[numPieces];
        for (int i = 0; i<numPieces; i++) {
            pieces[i] = new Piece(piecesString.substring(3*i,3*i+3),i);
        }

        Nook[] nooks = new Nook[18];
        for (int i = 0; i<18; i++) {
            nooks[i] = new Nook(nooksString.substring(4*i,4*i+3),nooksString.charAt(4*i +3));
        }

        Cranny[] crannies = new Cranny[6];
        for (int i = 0; i<6; i++) {
            crannies[i] = new Cranny(cranniesString.substring(3*i,3*i+3));
        }

        this.pieces = pieces;
        this.nooks = nooks;
        this.crannies = crannies;


    }

    /**
     * Progress the game-state by moving a piece in a particular direction
     */

    public int movePiece(int piece, Direction direction) {
        if (piece<0 || piece>= pieces.length) {
            System.out.println("INVALID MOVE"+piece);
            return 420;
        }
        int position = pieces[piece].position;
        XYcoord coordinate = new XYcoord(position);
        int x = coordinate.x;
        int y = coordinate.y;
        int nextX = 0;
        int nextY = 0;
        if (direction.value == 0) { // RIGHT
            nextX = coordinate.x+1;
            nextY = coordinate.y;
        }
        else if (direction.value == 1) { // UPRIGHT
            nextX = coordinate.x+1;
            nextY = coordinate.y+1;
        }
        else if (direction.value == 2) { // UPLEFT
            nextX = coordinate.x;
            nextY = coordinate.y+1;
        }
        else if (direction.value == 3) { // LEFT
            nextX = coordinate.x-1;
            nextY = coordinate.y;
        }
        else if (direction.value == 4) { // DOWNLEFT
            nextX = coordinate.x-1;
            nextY = coordinate.y-1;
        }
        else if (direction.value == 5) { // DOWNRIGHT
            nextX = coordinate.x;
            nextY = coordinate.y-1;
        }

        XYcoord nextXY = new XYcoord(nextX,nextY);
        int next = CoordinateTransform.getPosition(nextXY);
        int layer = CoordinateTransform.getLayer(position);
        int nextLayer = CoordinateTransform.getLayer(next);


        if (nextLayer>= 10) { // If moving an extra step would leave the board
            return position;
        }
        if (layer==9 && nextLayer == 9) {
            for (Cranny c : crannies) {
                if ((c.position == next && position > next && position!=216) || c.position == position && position<next || c.position == 216 && next == 169 && position == 216 || c.position==216 && next == 216 && position == 169) { // If moving an extra step would hit a cranny
                    return position;
                }
            }
        }
        for (Piece p : pieces) {
            if (p.position==next) {
                return position;
            }
        }
        for (Nook n : nooks) {
            for (Direction d : direction.oppositeDirections()) {
                if (n.position == position && n.direction == d) { // If we are already in a nook and try to move through the wall
                    return position;
                }
            }
            if (n.position == next) {
                for (Direction d : direction.oppositeDirections()) {
                    if (n.direction == d) { // If we are moving into the front (open) side of a nook
                        pieces[piece].position = next;
                        return next;
                    }
                }
                return position; // If we are moving into the back (closed) side of a nook
            }
        }
        pieces[piece].position = next;
        return movePiece(piece, direction);
    }


    /**
     * Construct HexGameGUI with a random game state that complies with the assignment specification.
     */
    public HexGame(int numPieces) {

        Random r = new Random();

        /**
         * First create a string which encodes a set of legitimate crannies
         */

        String cranniesString = "";
        while (legitimateCrannies(cranniesString)==false) {
            cranniesString = "";
            for (int i = 0; i < 6; i++) {
                cranniesString+= (Math.abs(r.nextInt()%8)+169+8*i);
            }
        }

        /**
         * Next we randomly generate nooks. The strategy we use is as follows:
         * If we pick 18 random nooks at once, the probability that those nooks are connected and
         * non-adjacent is very small,so instead we generate them three at a time (one triangle
         * segment of the board at a time). Every time we generate a new trio of nooks, we make sure that
         * the corresponding segment of the board doesn't contain any adjacent nooks or nooks in invalid positions.
         * After we have generated 6 groups of 3 nooks we then check the board for nook-conectedness. If the board isn't
         * connected we re-roll all the nooks.
         *
         */

        String nooksString = "";
        String s;

        String nooksTemplate = "010A098A102A" + "012A104A108A" + "014A114A110A" + "016A116A120A" + "018A126A122A" + "008A092A096A";

        /**
         * nooksTemplate is useful because we know it passes the legitimateNooks test, so when we
         * replace a 12-character segment of this string with three randomly generated nooks, applying
         * the legitimateNooks test to the modified string will only fail if the three nooks we just generated
         * were bad
         */

        boolean b = true;
        while (b) {
            nooksString = "";
            while (legitimateNooks(nooksString) == false) { // During this while loop we generate 3 nooks within the triangle with corners 10,98,102
                nooksString = nooksTemplate.substring(12);
                for (int i = 0; i < 3; i++) {
                    int x = Math.abs(r.nextInt()) % 5 + 2;
                    int y = Math.abs(r.nextInt()) % (x - 1) + 1;
                    int a = Math.abs(r.nextInt()) % 6;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }
            s = nooksString.substring(12);
            nooksString = s;
            while (legitimateNooks(nooksString) == false) { // During this while loop we generate 3 nooks within the triangle with corners 12,104,108
                nooksString = s;
                for (int i = 0; i < 3; i++) {
                    int x = Math.abs(r.nextInt()) % 5 + 1;
                    int y = -Math.abs(r.nextInt()) % (6 - x) - 1;
                    int a = Math.abs(r.nextInt()) % 6;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }
            s = nooksString.substring(12);
            nooksString = s;
            while (legitimateNooks(nooksString) == false) { // // During this while loop we generate 3 nooks within the triangle with corners 14,110,114
                nooksString = s;
                for (int i = 0; i < 3; i++) {
                    int x = -Math.abs(r.nextInt()) % 5 - 1;
                    int y = Math.abs(r.nextInt()) % (6 + x) - 6;
                    int a = Math.abs(r.nextInt()) % 6;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }
            s = nooksString.substring(12);
            nooksString = s;
            while (legitimateNooks(nooksString) == false) { // During this while loop we generate 3 nooks within the triangle with corners 16,116,120
                nooksString = s;
                for (int i = 0; i < 3; i++) {
                    int x = -Math.abs(r.nextInt()) % 5 - 2;
                    int y = -Math.abs(r.nextInt()) % (-1 - x) - 1;
                    int a = Math.abs(r.nextInt()) % 6;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }
            s = nooksString.substring(12);
            nooksString = s;
            while (legitimateNooks(nooksString) == false) { // During this while loop we generate 3 nooks within the triangle with corners 18,122,126
                nooksString = s;
                for (int i = 0; i < 3; i++) {
                    int x = -Math.abs(r.nextInt()) % 5 - 1;
                    int y = Math.abs(r.nextInt()) % (6 + x) + 1;
                    int a = Math.abs(r.nextInt()) % 6;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }
            s = nooksString.substring(12);
            nooksString = s;
            while (legitimateNooks(nooksString) == false) { // During this while loop we generate 3 nooks within the triangle with corners 8,92,96
                nooksString = s;
                for (int i = 0; i < 3; i++) {
                    int x = Math.abs(r.nextInt()) % 5 + 1;
                    int y = -Math.abs(r.nextInt()) % (6 - x) + 6;
                    int a = Math.abs(r.nextInt()) % 6;
                    char c;
                    XYcoord xy = new XYcoord(x, y);
                    nooksString += xy.positionAsString() + Direction.convertIntToChar(a);
                }
            }

            if (legitimateGame(cranniesString + nooksString + nooksString.substring(0, 3))) { // If the board is solvable, stop looping, otherwise try completely new set of nooks
                b = false;
            }
        }

        /**
         * Now we just need to randomly assign pieces to nooks
         */

        String piecesString = "";
        int[] occupiedNooks = new int[4];
        for (int i=0; i<numPieces; i++) {
            boolean a = true;
            while (a) {
                int nookNumber = Math.abs(r.nextInt())%18;
                if (!ArrayMethods.contains(occupiedNooks,nookNumber)) {
                    occupiedNooks[i]=nookNumber;
                    piecesString += nooksString.substring(nookNumber*4, nookNumber*4+3);
                    a = false; // As long as we put the piece in a nook that wasn't already occupied, we move on to next piece
                }
            }
        }


        String gamestring=cranniesString+nooksString+piecesString;
        HexGame dummyGame = new HexGame(gamestring);

        this.pieces = dummyGame.pieces;
        this.nooks = dummyGame.nooks;
        this.crannies = dummyGame.crannies;

    }

    public HexGame() {
        Random r = new Random();
        HexGame dummyGame = new HexGame(r.nextInt(4)+1);
        this.pieces = dummyGame.pieces;
        this.nooks = dummyGame.nooks;
        this.crannies = dummyGame.crannies;
    }

    /**
     * Determine whether a set of pieces are legal according to the assignment specification. (Disregarding whether they are in nooks).
     * @param pieces A string describing the nooks, encoded according to the assignment specification.
     * @return True if the pieces are correctly encoded and in legal positions, according to the assignment specification.
     */
    public static boolean legitimatePieces (String pieces) {
        if (pieces.length()==0 || pieces.length()%3 != 0) {
            return false;
        }
        int numPieces = pieces.length()/3;
        Piece[] listOfPieces = new Piece[numPieces];

        for (int i = 0; i<numPieces; i++) {
            listOfPieces[i] = new Piece(pieces.substring(3*i,3*i+3),i);
        }
        for (Piece p : listOfPieces) {
            if (p.position<0 || p.position>216) {
                return false;
            }
        }

        for (int i = 0; i<numPieces; i++) {
            for (int j = i+1; j<numPieces; j++) {
                if (listOfPieces[i].position == listOfPieces[j].position) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determine whether a set of crannies are legal according to the assignment specification.
     * @param crannies A string describing the crannies, encoded according to the assignment specification.
     * @return True if the crannies are correctly encoded and in legal positions, according to the assignment specification.
     */
    public static boolean legitimateCrannies(String crannies) {
        if (crannies.length() != 18) {
            return false;
        }
        else {
            boolean a = true;
            int[] listOfCrannies = new int[6];
            for (int i = 0; i<6; i++) {
                listOfCrannies[i] = Integer.parseInt(crannies.substring(0,3));
                crannies = crannies.substring(3);
            }
            for (int cranny : listOfCrannies) {
                if (cranny > 216 || cranny < 169) { // Making sure all crannies are on the outer layer of the board
                    a = false;
                }}
            for (int i = 0; i<6; i++) {
                for (int j = i+1; j<6; j++) {
                    if ((listOfCrannies[i]-169)/8==(listOfCrannies[j]-169)/8) { // Making sure two crannies never share an edge
                        a = false;
                    }}
            }
            return a;
        }

    }

    /**
     * Determine whether a set of nooks are legal according to the assignment specification. (Disregarding whether they are connected)
     * @param nooks A string describing the nooks, encoded according to the assignment specification.
     * @return True if the nooks are correctly encoded and in legal positions, according to the assignment specification.
     */
    public static boolean legitimateNooks(String nooks) {
        if (nooks.length() != 18 * 4) { // Make sure there's 18 nooks
            return false;
        }

        Nook[] listOfNooks = new Nook[18];
        for (int i = 0; i < 18; i++) {
            if (nooks.charAt(4 * i + 3) < 'A' || nooks.charAt(4 * i + 3) > 'F') { // Make sure every nook is encoded with a legitimate direction
                return false;
            }
            listOfNooks[i] = new Nook(nooks.substring(4 * i, 4 * i + 3), nooks.charAt(4 * i + 3));
        }

        for (int i = 0; i<18; i++) {
            for (int j = i+1; j<18; j++) {
                XYcoord iXY = new XYcoord(listOfNooks[i].position);
                XYcoord jXY = new XYcoord(listOfNooks[j].position);
                if (       (iXY.x == jXY.x   && iXY.y   == jXY.y)  // Make sure no nooks are in the same position or adjacent positions
                        || (iXY.x == jXY.x+1 && iXY.y   == jXY.y)
                        || (iXY.x == jXY.x   && iXY.y+1 == jXY.y)
                        || (iXY.x == jXY.x+1 && iXY.y-1 == jXY.y)
                        || (iXY.x == jXY.x-1 && iXY.y   == jXY.y)
                        || (iXY.x == jXY.x   && iXY.y-1 == jXY.y)
                        || (iXY.x == jXY.x-1 && iXY.y+1 == jXY.y)) {
                    return false;
                }
            }
        }

        for (Nook n : listOfNooks) {
            if (CoordinateTransform.getLayer(n.position) <2 || CoordinateTransform.getLayer(n.position)>7 ) { // Make sure all nooks are with layers 2-7
                return false;
            }
        }

        /*
         * The condition that there are three nooks in each triangular segment of the board is equivalent
         * to the condition that there be 9 nooks in each half of the board (the board can be cut in half along
         * the x-axis, along the y-axis, or, along the line y=x).
         */

        int nooksRightOfYAxis = 0;
        int nooksLeftOfYAxis = 0;
        int nooksAboveXAxis = 0;
        int nooksBelowXAxis = 0;
        int nooksRightOfLineYEqualsX = 0;
        int nooksLeftOfLineYEqualsX = 0;

        for (Nook n : listOfNooks) {
            XYcoord coordinates = new XYcoord(n.position);
            int x = coordinates.x;
            int y = coordinates.y;
            if (x > 0)
                nooksRightOfYAxis ++;
            if (x < 0)
                nooksLeftOfYAxis ++;
            if (y > 0)
                nooksAboveXAxis ++;
            if (y < 0)
                nooksBelowXAxis ++;
            if (x-y > 0)
                nooksRightOfLineYEqualsX++;
            if (x-y < 0)
                nooksLeftOfLineYEqualsX ++;
        }


        if (nooksRightOfYAxis != 9)
            return false;
        if (nooksLeftOfYAxis != 9)
            return false;
        if (nooksAboveXAxis != 9)
            return false;
        if (nooksBelowXAxis != 9)
            return false;
        if (nooksRightOfLineYEqualsX != 9)
            return false;
        if (nooksLeftOfLineYEqualsX != 9)
            return false;

        return true;
    }

    /**
     * Determine whether a game state is legal according to the assignment specification.
     * @param game A string describing the game state, encoded according to the assignment specification.
     * @return True if the game state is correctly encoded and represents a legal game state, according to the assignment specification.
     */
    public static boolean legitimateGame(String game) {
        HexGame hexGame = new HexGame(game);
        String cranniesString = game.substring(0,18);
        String nooksString = game.substring(18,90);
        String piecesString = game.substring(90);

        if (!legitimateNooks(nooksString)) {
            return false;
        }
        if (!legitimateCrannies(cranniesString)) {
            return false;
        }
        if (!legitimatePieces(piecesString)) {
            return false;
        }

        /*
         * If legitimateNooks, legitimateCrannies and legitimatePieces all pass,
         * we still need to check that pieces are in nooks, and that the nooks are
         * connected.
         */


       //Make sure every piece is in a nook:
        for (Piece p : hexGame.pieces) {
            boolean inNook = false;
            for (Nook n : hexGame.nooks) {
                if( n.position == p.position) {
                    inNook=true;
                }
            }
            if (!inNook) {
                return false;
            }
        }

//      Make sure all nooks are connected:
        String positionOfNook0 = hexGame.nooks[0].toString().substring(0,3);
        for (int i = 0; i<18; i++) {
            String positionOfNooki = hexGame.nooks[i].toString().substring(0,3);
            if (minimalPath(game.substring(0,90)+positionOfNooki, hexGame.nooks[i].position,hexGame.nooks[0].position).length > 19) { // Make sure there is a path from nook i to nook 0
                return false;
            }
            if (minimalPath(game.substring(0,90)+positionOfNook0, hexGame.nooks[0].position,hexGame.nooks[i].position).length > 19) { // Make sure there is a path from nook 0 to nook i
                return false;
            }
        }

        return true;
    }

    /**
     * Determine whether a given step is legal according to a given game state and the assignment specification.
     * @param game A string describing the game state, encoded according to the assignment specification.
     * @param from The point from which the step starts
     * @param to The point to which step goes
     * @return True if the move is legal according to the assignment specification.
     */
    public static boolean legitimateStep(String game, int from, int to) {
        HexGame hexGame = new HexGame(game);
        for (Piece p : hexGame.pieces) {
            if (p.position == from) {
                for (Direction d : Direction.allDirections) {
                    if (SimulateMove.simulateMove(hexGame,p.number, d)== to) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return a minimal path from start to goal.
     * @param game A string describing the game state, encoded according to the assignment specification.
     * @param start The point from which the path must start
     * @param goal The point at which the path must end
     * @return An array of integers reflecting a minimal path from start to goal, each integer reflecting a node on the board, starting with the start, and ending with the goal.
     */

    public static int[] minimalPath(String game, int start, int goal) {

        /*
         * First off, to make things simpler, the string which describes the game-state will be modified
         * so that the moving piece is encoded before any stationary pieces
         */

        HexGame hexGame = new HexGame(game);
        boolean pieceAtStart = false;
        String gameWithPieceAtFront = "";
        for (Piece p : hexGame.pieces) {
            if (p.position == start) {
                pieceAtStart = true;
                gameWithPieceAtFront = game.substring(0,90) + game.substring(90+3*p.number, 90+3*p.number +3) + game.substring(90,90+3*p.number) + game.substring(90+3*p.number +3);
            }
        }
        int[] symbolOfFailure = new int[20]; // return this when there is no feasible path
        for (int i = 0; i<20; i++) {
            symbolOfFailure[i]=0;
        }

        if (!pieceAtStart) { // If no piece was found at the starting position...
            return symbolOfFailure;
        }

        int[] moveZero = {start};
        ArrayList<int[]> moveHistories = new ArrayList<>();
        moveHistories.add(moveZero);

        boolean[] positionsVisited = new boolean[217];
        for (int i = 0; i<positionsVisited.length; i++) {
            positionsVisited[i]=false;
        }
        positionsVisited[start] = true;

        return minimalPathRecursive(gameWithPieceAtFront, goal, moveHistories, positionsVisited);


    }

    public static int[] minimalPathRecursive(String game, int goal, ArrayList<int[]> moveHistories, boolean[] positionsVisited) {
        /*
         * Execute a breadth-first search of the game tree until a viable path is found.
         * The third argument of this function is an array of all possible paths of the current depth
         * which might lead to the goal.
         */

        int[] symbolOfFailure = new int[20]; // return this when there is no feasible path
        for (int i = 0; i<20; i++) {
            symbolOfFailure[i]=0;
        }

        boolean somethingChanged = false;

        for (int[] path : moveHistories) {
            if (path[path.length-1] == goal) { // Check if any of the paths have reached the goal yet
                return path;
            }
        }

        ArrayList<int[]> nextMoveHistories = new ArrayList<>();

        for (int[] path : moveHistories) {
            String s = ""+path[path.length-1];
            while (s.length()<3) {
                s = "0"+s;
            }
            HexGame hexGame = new HexGame(game.substring(0,90)+s+game.substring(93)); // Generate a new HexGameGUI with the piece in the location it would be after we make all previous moves in the path we're exploring

            for (Direction d : Direction.allDirections) {
                int nextPosition = SimulateMove.simulateMove(hexGame, 0, d);
                if (!positionsVisited[nextPosition]) { // We only want to expand a path in a particular direction if it takes us somewhere new
                    positionsVisited[nextPosition] = true;
                    somethingChanged = true;
                    int[] extendedPath = new int[path.length+1];
                    for (int i = 0; i<path.length; i++) {
                        extendedPath[i] = path[i]; // Copying the path into a new array which has room for an extra move
                    }
                    extendedPath[path.length] = SimulateMove.simulateMove(hexGame, 0, d);
                    nextMoveHistories.add(extendedPath);
                }
            }
        }

        if (!somethingChanged) // If no new positions have been explored this iteration, then an exhaustive search has been completed without finding any feasible path
            return symbolOfFailure;

        return minimalPathRecursive(game, goal, nextMoveHistories, positionsVisited);
    }


    public static class Move {
        public int piece;
        public Direction direction;

        public Move(int piece, Direction direction) {
            this.piece = piece;
            this.direction = direction;
        }
    }

    static public class possibleBoardState {
        LinkedList<Move> moveHistory;
        HexGame board;

        public possibleBoardState(LinkedList<Move> moveHistory, HexGame board) {
            this.moveHistory = moveHistory;
            this.board = board;
        }
    }

    public static LinkedList<Move> properMinimalPath (HexGame game, int goal) {
        if (game.getPieces().length==1) {
            LinkedList<Move> rtn = new LinkedList<>();
            int[] path = minimalPath(game.toString(),game.getPieces()[0].getPosition(),goal);
            HexGame dummyGame = new HexGame(game.toString());
            for (int i = 1; i<path.length; i++) {
                for (Direction d : Direction.allDirections) {
                    if (SimulateMove.simulateMove(dummyGame,0,d)==path[i]) {
                        dummyGame.movePiece(0, d);
                        rtn.add(new Move(0,d));
                        break;
                    }
                }
            }
            return rtn;
        }
        ArrayList<LinkedList<Move>> moveHistories = new ArrayList<>();
        for (Piece p : game.getPieces()) {
            if (p.getPosition() == goal) {
                LinkedList<Move> rtn = new LinkedList<>();
                return rtn;
            }
        }
        HexGame dummyGame = new HexGame(game.toString());
        ArrayList<possibleBoardState> branches = new ArrayList<>();
        LinkedList<Move> moveHistory = new LinkedList<>();
        branches.add(new possibleBoardState(moveHistory, dummyGame));
        return properMinimalPathRecursive(goal, branches, game);
    }

    static public LinkedList<Move> properMinimalPathRecursive (int goal, ArrayList<possibleBoardState> branches, HexGame originalGame) {
        if (branches.size()!=0) {
            if (branches.get(0).moveHistory.size()>=3) { // at this point the tree is getting big enough to slow the game down, so start looking for ways to solve the game while only using one piece
                int[] singlePiecePath = new int[15];
                int pathLength = 20;
                int piece = 0;
                for (int i = 0; i<originalGame.getPieces().length; i++) {
                    int[] candidatePath = minimalPath(originalGame.toString(),originalGame.getPieces()[i].getPosition(),goal);
                    if (candidatePath.length <= pathLength) {
                        singlePiecePath = candidatePath;
                        pathLength = candidatePath.length;
                        piece = i;
                    }
                }


                LinkedList<Move> rtn = new LinkedList<>();
                HexGame dummyGame = new HexGame(originalGame.toString());
                for (int i = 1; i<singlePiecePath.length; i++) {
                    for (Direction d : Direction.allDirections) {
                        if (SimulateMove.simulateMove(dummyGame,piece,d)==singlePiecePath[i]) {
                            dummyGame.movePiece(piece, d);
                            rtn.add(new Move(piece,d));
                            break;
                        }
                    }
                }
                return rtn;
            }
        }
        ArrayList<possibleBoardState> newBranches = new ArrayList<>();
        for (possibleBoardState possibleState : branches) {
            for (int p = 0; p< possibleState.board.getPieces().length; p++) {
                for (Direction d : Direction.allDirections) {
                    if (SimulateMove.simulateMove(possibleState.board, p,d) == goal) {
                        possibleState.moveHistory.add(new Move(p,d));
                        return possibleState.moveHistory;
                    }
                    else if (SimulateMove.simulateMove(possibleState.board, p, d) != possibleState.board.getPieces()[p].getPosition()) {
                        HexGame nextBoard = new HexGame(possibleState.board.toString());
                        nextBoard.movePiece(p,d);
                        LinkedList<Move> nextMoveHistory = new LinkedList<>();
                        for (Move move : possibleState.moveHistory) {
                            nextMoveHistory.add(move);
                        }
                        nextMoveHistory.add(new Move(p,d));
                        newBranches.add(new possibleBoardState(nextMoveHistory,nextBoard));
                    }
                }
            }

        }
        return properMinimalPathRecursive(goal, newBranches, originalGame);
    }

    /**
     * Output the state of the game as a string, encoded according to the assignment specification
     * @return A string that reflects the game state, encoded according to the assignment specification.
     */
    public String toString() {
        String s = "";
        for (Cranny c : crannies) {
            s += c.toString();
        }
        for (Nook n : nooks) {
            s += n.toString();
        }
        for (Piece p : pieces) {
            s += p.toString();
        }
        return s;
    }

    public static void main(String[] args) {
        LinkedList<Move> path = properMinimalPath(new HexGame("171178187194205215093D038D064E070C100D043D106A108F072A080A051D112F082B016C118D060D125B122D016083"),72);
        System.out.println(path.size());
        System.out.println(path.get(0).direction);
        System.out.println(path.get(0).piece);
        System.out.println(path.get(1).direction);
        System.out.println(path.get(1).piece);
// Green, Red, Yellow, Blue
    }
}

