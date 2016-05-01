package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by X4 on 9/10/2015.
 */

public class HexGameGUI extends Application {

    public static final double SIZE = 22;
    public static int activePiece = 0;
    public comp1110.ass2.HexGame hexGame;
    public int goalPosition;

    Random random = new Random();

    int numMoves;
    int score;
    int par;
    int currentRound;
    int numRounds;
    int numPieces;

    boolean gameBusy;
    boolean clockStarted = true;
    boolean hintUsed = false;
    long offset;
    Text timeText = new Text();
    Text timeHeading = new Text("TIME             ");
    Text movesText = new Text();
    Text movesHeading = new Text("MOVES        ");
    Text hintText = new Text("Hint");
    Text roundHeading = new Text("ROUND        ");
    Text roundText = new Text();
    Text scoreText = new Text();
    Text scoreHeading = new Text("SCORE         ");

    Circle Hbutton;

    double mouseX;
    double mouseY;

    double ghostX;
    double ghostY;
    double boardX;
    double boardY;

    long elapsedSeconds = 0;
    double scale;
    String seconds;
    String minutes;

    LinkedList<HexGame.Move> minimalPath;

    /* JavaFX object groups */

    private Group root = new Group();
    private Group board = new Group();
    private Group UI = new Group();
    private StackPane timeDisplay = new StackPane();
    private StackPane moveDisplay = new StackPane();
    private StackPane hintButton = new StackPane();
    private StackPane roundDisplay = new StackPane();
    private StackPane scoreDisplay = new StackPane();

    private Group pieces = new Group();
    private Group highlights = new Group();
    private Group ghost = new Group();
    private Group hint = new Group();
    private Group goal = new Group();
    private Group winScreen = new Group();
    private Group fireworks = new Group();

    Scene scene = new Scene(root, 700, 700);

    // Hightlights are flashing hexagons which indicate where a piece can move to
    public class Highlight extends Hexagon {
        Direction direction;
        public Highlight(double centerX, double centerY, double size, Direction direction) {
            super(centerX,centerY,size);
            this.direction = direction;
            // Make colour match the colour of the piece the user has selected
            switch (activePiece) {
                case 0: this.setFill(Color.FORESTGREEN);
                    break;
                case 1: this.setFill(Color.RED);
                    break;
                case 2: this.setFill(Color.YELLOW);
                    break;
                case 3: this.setFill(Color.BLUE);
                    break;
            }
            // Make the highlight flash
            FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
            ft.setFromValue(0.6);
            ft.setToValue(0.0);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
            // If the user right-clicks on a highlight, move the piece to that highlight
            this.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.SECONDARY)) movePiece(direction);
            });
        }
    }

    public class Piece extends Circle {
        public Piece(double boardCenterX, double boardCenterY, double size, int pieceNumber, int position) {
            super(0,0,size*0.6);
            // Set the color
            switch (pieceNumber%4) {
                case 0: this.setFill(Color.FORESTGREEN);
                    break;
                case 1: this.setFill(Color.RED);
                    break;
                case 2: this.setFill(Color.YELLOW);
                    break;
                case 3: this.setFill(Color.BLUE);
                    break;
            }

            XYcoord coordinate = new XYcoord(position);
            this.setLayoutX((coordinate.x - 0.5 * coordinate.y) * 2 * size * 0.866 + boardCenterX);
            this.setLayoutY(-coordinate.y * size * 1.5 + boardCenterY);
            // When the user right clicks a piece, redraw highlights according to which piece was pressed
            this.setOnMousePressed(event -> {
                if (!gameBusy && event.getButton().equals(MouseButton.SECONDARY)) {
                    activePiece = pieceNumber;
                    drawHighlights();
                    // Set the variables below so that user can click-and-drag the piece
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                    ghostX = this.getLayoutX();
                    ghostY = this.getLayoutY();
                    ghost.getChildren().add(new Ghost(pieceNumber));
                }

            });
            // click-and-drag code below:
            this.setOnMouseDragged(event -> {
                if (!gameBusy && event.getButton().equals(MouseButton.SECONDARY)) {
                    double mouseOffsetX = (event.getSceneX() - mouseX)/scale;
                    double mouseOffsetY = (event.getSceneY() - mouseY)/scale;

                    ghostX += mouseOffsetX;
                    ghostY += mouseOffsetY;

                    ghost.getChildren().get(0).setLayoutX(ghostX);
                    ghost.getChildren().get(0).setLayoutY(ghostY);
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                }
            });

            this.setOnMouseReleased(event -> {
                if (!gameBusy && event.getButton().equals(MouseButton.SECONDARY)) {
                    // Check to see if the ghost was dropped on a highlight, if it was, move the piece
                    for (Node highlight : highlights.getChildren()) {
                        double dx = ((Highlight) highlight).centerX - ghostX;
                        double dy = ((Highlight) highlight).centerY - ghostY;
                        if (dx * dx + dy * dy <= size * size) {
                            movePiece(((Highlight) highlight).direction);
                            break;
                        }
                    }
                    ghost.getChildren().clear();
                }
            });

        }
    }

    //ghosts are transparent circles which follow the mouse arround when the user is moving a piece using click-and-drag
    public class Ghost extends Circle {
        public Ghost(int player) {
            super(0,0,SIZE*0.6);
            double centerX = pieces.getChildren().get(player).getLayoutX();
            double centerY = pieces.getChildren().get(player).getLayoutY();
            this.setLayoutX(centerX);
            this.setLayoutY(centerY);
            switch (player%4) {
                case 0: this.setFill(Color.FORESTGREEN);
                    break;
                case 1: this.setFill(Color.RED);
                    break;
                case 2: this.setFill(Color.YELLOW);
                    break;
                case 3: this.setFill(Color.BLUE);
                    break;
            }
            this.setOpacity(0.5);
        }
    }

    // a Goal is a pulsing, colour changing marker which shows the player where they are trying to get the pieces to
    public class Goal extends Hexagon{
        public Goal(double centerX, double centerY, double size) {
            super(centerX, centerY, size);

            // Make the goal grow+shrink
            ScaleTransition st = new ScaleTransition(Duration.millis(500), this);
            st.setByX(0.3);
            st.setByY(0.3);
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);
            st.play();

            // Make the goal cylce between opaque and transparent
            FadeTransition ft = new FadeTransition(Duration.millis(1700),this);
            ft.setFromValue(0.2);
            ft.setToValue(0.8);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();

            //make the goal change colour
            Animation colourChange = new Transition() {
                {
                    setCycleDuration(Duration.millis(5420));
                    setCycleCount(INDEFINITE);
                }

                protected void interpolate(double frac) {
                    Goal.this.setFill(Color.hsb(350*frac, 1, 1));

                }

            };
            colourChange.play();

        }
    }


    void drawHighlights() {
        if (gameBusy) return;
        // remove current highlights
        highlights.getChildren().clear();

        int currentPosition = hexGame.getPieces()[activePiece].getPosition();

        // put a highlight in any new place the piece could move
        for (Direction d : Direction.allDirections) {
            if (SimulateMove.simulateMove(hexGame, activePiece,d) != currentPosition) {
                XYcoord coordinate = new XYcoord(SimulateMove.simulateMove(hexGame, activePiece,d));
                highlights.getChildren().add(new Highlight((coordinate.x-0.5*coordinate.y)*2*SIZE*0.866+350, -coordinate.y*SIZE*1.5+350, SIZE*0.95, d));
            }
            else {
                highlights.getChildren().add(new Highlight(350,350,0, d)); // these are invisible (size = 0)
            }
        }
    }

    void showHint() {
        if (gameBusy) return;
        hintUsed = true;
        HexGame.Move move = minimalPath.get(0); // the first move in the minimalPath
        hint.getChildren().add(new HintArrow(350, 350, SIZE, hexGame.getPieces()[move.piece].getPosition(), move.piece, move.direction));
        Hbutton.setFill(Color.web("#FF9933"));
    }

    void hideHint() {
        if (gameBusy) return;
        hint.getChildren().clear();
        Hbutton.setFill(Color.BLANCHEDALMOND);
    }

    void movePiece(Direction direction) {
        if (gameBusy) return;
        gameBusy = true;
        hint.getChildren().clear();
        PlaySound1();

        numMoves ++;
        movesText.setText("             " + numMoves);
        // If the player has taken more moves than necessary, increment the score
        if (numMoves > par) {
            score++;
            scoreText.setText("               " + score);
        }
        // Animate the piece moving
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        Hexagon target = (Hexagon) highlights.getChildren().get(direction.value);
        highlights.getChildren().clear();
        path.getElements().add(new LineTo(target.getLayoutX() - pieces.getChildren().get(activePiece).getLayoutX() + 0.866 * SIZE, target.getLayoutY() - pieces.getChildren().get(activePiece).getLayoutY() + 0.5 * SIZE));

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(400));
        pt.setNode(pieces.getChildren().get(activePiece));
        pt.setPath(path);
        pt.play();
        pt.setOnFinished(event1 -> {
            // When the animation is finished, update the hexGame, destroy the piece, and make a new one according to the hexGame
            hexGame.movePiece(activePiece, direction);
            pieces.getChildren().remove(activePiece);
            minimalPath = HexGame.properMinimalPath(hexGame, goalPosition);
            comp1110.ass2.Piece p = hexGame.getPieces()[activePiece];
            pieces.getChildren().add(activePiece, new Piece(350, 350, SIZE, p.getNumber(), p.getPosition()));
            // If the piece hit the goal start a new round
            if (hexGame.getPieces()[activePiece].getPosition() == goalPosition) newRound();
            else gameBusy = false;

        });

    }

    void newGoal() {
        goal.getChildren().clear();
        //Randomly pick an unocupied nook to be the goal
        while (goal.getChildren().size()==0) {
            int i = random.nextInt(18);
            boolean pieceAtNooki = false;
            for (comp1110.ass2.Piece p : hexGame.getPieces()) {
                if (p.getPosition() == hexGame.getNooks()[i].getPosition()) pieceAtNooki = true;
            }
            if (!pieceAtNooki) {
                goalPosition = hexGame.getNooks()[i].getPosition();
                XYcoord coordinate = new XYcoord(goalPosition);
                goal.getChildren().add(new Goal((coordinate.x-0.5*coordinate.y)*2*SIZE*0.866+350, -coordinate.y*SIZE*1.5+350, SIZE));

            }
        }
        minimalPath = HexGame.properMinimalPath(hexGame,goalPosition);
    }

    void newRound() {
        if (currentRound==numRounds) {
            gameWon();
            return;
        }
        newGoal();
        par = numMoves+minimalPath.size();
        currentRound++;
        roundText.setText("              " + currentRound + "/" + numRounds);
        gameBusy = false;

    }


    void startGame(int numPieces, int numRounds) {

        //clear all the groups (in-case stuff is leftover from last game)
        root.getChildren().clear();
        PlaySound3();
        pieces.getChildren().clear();
        board.getChildren().clear();
        goal.getChildren().clear();
        UI.getChildren().clear();
        timeDisplay.getChildren().clear();
        moveDisplay.getChildren().clear();
        hintButton.getChildren().clear();
        roundDisplay.getChildren().clear();
        scoreDisplay.getChildren().clear();
        highlights.getChildren().clear();
        ghost.getChildren().clear();
        hint.getChildren().clear();
        goal.getChildren().clear();
        winScreen.getChildren().clear();
        fireworks.getChildren().clear();

        gameBusy=false;
        board.setOpacity(1);
        hintUsed = false;

        //Create the hexGame
        hexGame = new HexGame(numPieces);
        newGoal();

        minimalPath = HexGame.properMinimalPath(hexGame,goalPosition);
        numMoves = 0;
        this.numRounds = numRounds;
        this.numPieces = numPieces;
        score = 0;
        par = minimalPath.size();
        currentRound = 1;
        movesText.setText("             " + numMoves);
        roundText.setText("              "+currentRound+"/"+ this.numRounds);
        scoreText.setText("               " + score);
        scale = 0.9;

        // This AnimationTimer is what makes the clock tick
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (clockStarted) {
                    offset=now;
                    clockStarted = false;
                }
                elapsedSeconds = ((long) Math.floor(now-offset))/1000000000;
                seconds = ""+elapsedSeconds%60;
                if (seconds.length()<2) seconds = "0" + seconds;
                minutes = ""+elapsedSeconds/60;
                if (minutes.length()<2) minutes = "0" + minutes;
                timeText.setText("          " + minutes + ":" + seconds);

            }
        }.start();


        //Put board and pieces on the stage
        board.getChildren().add(new DrawBoard(hexGame, SIZE, 350, 350));
        for (comp1110.ass2.Piece p: hexGame.getPieces()) {
            pieces.getChildren().add(new Piece(350,350,SIZE,p.getNumber(),p.getPosition()));
        }


        board.getChildren().add(pieces);
        board.getChildren().add(goal);
        board.getChildren().add(highlights);
        board.getChildren().add(ghost);
        board.getChildren().add(hint);

        board.setLayoutY(-50);
        UI.setLayoutX(10);
        UI.setLayoutY(570);
        root.getChildren().add(board);
        root.getChildren().add(UI);


        Rectangle UIBackground = new Rectangle(0,0,680,120);
        UIBackground.setArcHeight(30);
        UIBackground.setArcWidth(30);
        UIBackground.setFill(Color.web("#CB9245"));
        UI.getChildren().add(UIBackground);

        Rectangle clockBackground = new Rectangle(200,45);
        clockBackground.setArcHeight(30);
        clockBackground.setArcWidth(30);
        clockBackground.setFill(Color.web("#DEB887"));

        timeText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timeText.setTextAlignment(TextAlignment.CENTER);
        timeHeading.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        timeHeading.setTextAlignment(TextAlignment.CENTER);

        timeDisplay.getChildren().add(clockBackground);
        timeDisplay.getChildren().add(timeHeading);
        timeDisplay.getChildren().add(timeText);
        timeDisplay.setLayoutX(470);
        timeDisplay.setLayoutY(65);
        UI.getChildren().add(timeDisplay);


        Rectangle moveCounterBackground = new Rectangle(200,45);
        moveCounterBackground.setArcHeight(30);
        moveCounterBackground.setArcWidth(30);
        moveCounterBackground.setFill(Color.web("#DEB887"));


        movesText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        movesText.setTextAlignment(TextAlignment.CENTER);
        movesHeading.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        movesHeading.setTextAlignment(TextAlignment.CENTER);

        moveDisplay.getChildren().add(moveCounterBackground);
        moveDisplay.getChildren().add(movesHeading);
        moveDisplay.getChildren().add(movesText);
        moveDisplay.setLayoutX(470);
        moveDisplay.setLayoutY(10);
        UI.getChildren().add(moveDisplay);

        Rectangle roundCounterBackground = new Rectangle(200,45);
        roundCounterBackground.setArcHeight(30);
        roundCounterBackground.setArcWidth(30);
        roundCounterBackground.setFill(Color.web("#DEB887"));

        roundText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        roundText.setTextAlignment(TextAlignment.CENTER);
        roundHeading.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        roundHeading.setTextAlignment(TextAlignment.CENTER);

        roundDisplay.getChildren().add(roundCounterBackground);
        roundDisplay.getChildren().add(roundHeading);
        roundDisplay.getChildren().add(roundText);
        roundDisplay.setLayoutX(260);
        roundDisplay.setLayoutY(10);
        UI.getChildren().add(roundDisplay);

        Rectangle scoreBackground = new Rectangle(200,45);
        scoreBackground.setArcHeight(30);
        scoreBackground.setArcWidth(30);
        scoreBackground.setFill(Color.web("#DEB887"));

        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        scoreText.setTextAlignment(TextAlignment.CENTER);
        scoreHeading.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        scoreHeading.setTextAlignment(TextAlignment.CENTER);

        scoreDisplay.getChildren().add(scoreBackground);
        scoreDisplay.getChildren().add(scoreHeading);
        scoreDisplay.getChildren().add(scoreText);
        scoreDisplay.setLayoutX(260);
        scoreDisplay.setLayoutY(65);
        UI.getChildren().add(scoreDisplay);






        Hbutton = new Circle(45);
        hintText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        hintText.setTextAlignment(TextAlignment.CENTER);
        Hbutton.setFill(Color.BLANCHEDALMOND);
        hintButton.getChildren().add(Hbutton);
        hintButton.getChildren().add(hintText);
        hintButton.setLayoutX(10);
        hintButton.setLayoutY(15);
        UI.getChildren().add(hintButton);
        Hbutton.setOnMousePressed(event -> showHint());
        Hbutton.setOnMouseReleased(event -> hideHint());
        hintText.setOnMousePressed(event -> showHint());
        hintText.setOnMouseReleased(event -> hideHint());

        Rectangle quitButton = new Rectangle(140,100);
        quitButton.setLayoutX(110);
        quitButton.setLayoutY(10);
        quitButton.setArcHeight(30);
        quitButton.setArcWidth(30);
        quitButton.setFill(Color.BLANCHEDALMOND);
        UI.getChildren().add(quitButton);

        Text quitText = new Text("QUIT");
        quitText.setTextAlignment(TextAlignment.CENTER);
        quitText.setWrappingWidth(140);
        quitText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        quitText.setLayoutX(110);
        quitText.setLayoutY(75);
        UI.getChildren().add(quitText);

        quitButton.setOnMouseClicked(event -> mainMenu());
        quitText.setOnMouseClicked(event -> mainMenu());




        board.setScaleX(scale);
        board.setScaleY(scale);

        // Click-and-drag to move the board around
        board.setOnMousePressed(event -> {
            if (!gameBusy && event.getButton().equals(MouseButton.PRIMARY)) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                boardX = board.getLayoutX();
                boardY = board.getLayoutY();
            }

        });

        board.setOnMouseDragged(event -> {
            if (!gameBusy && event.getButton().equals(MouseButton.PRIMARY)) {
                double mouseOffsetX = event.getSceneX() - mouseX;
                double mouseOffsetY = event.getSceneY() - mouseY;
                if (-200*scale<boardX+mouseOffsetX&&boardX+mouseOffsetX<200*scale&&-200*scale<boardY+mouseOffsetY&&boardY+mouseOffsetY<150*scale) {
                    boardX += mouseOffsetX;
                    boardY += mouseOffsetY;
                    board.setLayoutX(boardX);
                    board.setLayoutY(boardY);
                }

                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });

        //zoom in and out by scrolling mouse wheel
        root.setOnScroll(event -> {
            if(!gameBusy) {
                if (event.getDeltaY() >= 0 && board.getScaleX() <= 2) {

                    scale = scale*1.1;
                    board.setScaleX(scale);
                    board.setScaleY(scale);
                }
                if (event.getDeltaY() <= 0 && board.getScaleY() > 0.9) {

                    scale = scale/1.1;
                    board.setLayoutX(board.getLayoutX()/1.3);
                    board.setLayoutY((board.getLayoutY()+50)/1.3-50);
                    board.setScaleX(scale);
                    board.setScaleY(scale);
                }
            }
        });
    }

    void gameWon() {
        gameBusy=true;
        board.setOpacity(0.5);
        fireworks.getChildren().clear();

        Text finalTime = new Text(timeText.getText());
        finalTime.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timeDisplay.getChildren().remove(timeText);
        timeDisplay.getChildren().add(finalTime);
        Rectangle winScreenTopBorder = new Rectangle(600,180);
        winScreenTopBorder.setFill(Color.web("#4C1A00"));
        winScreenTopBorder.setLayoutX(50);
        winScreenTopBorder.setLayoutY(30);
        winScreenTopBorder.setArcHeight(30);
        winScreenTopBorder.setArcWidth(30);
        winScreen.getChildren().add(winScreenTopBorder);

        Rectangle winScreenTopPanel = new Rectangle(580,160);
        winScreenTopPanel.setLayoutX(60);
        winScreenTopPanel.setLayoutY(40);
        winScreenTopPanel.setArcHeight(30);
        winScreenTopPanel.setArcWidth(30);
        winScreenTopPanel.setFill(Color.BLANCHEDALMOND);
        winScreen.getChildren().add(winScreenTopPanel);

        Text congratulations = new Text("Congratulations! \n YOU WON!");
        congratulations.setFont(Font.font("Arial", FontWeight.BOLD, 65));
        congratulations.setFill(Color.web("#4C1A00"));
        congratulations.setLayoutX(70);
        congratulations.setWrappingWidth(570);
        congratulations.setLayoutY(110);
        congratulations.setTextAlignment(TextAlignment.CENTER);
        winScreen.getChildren().add(congratulations);

        Rectangle winScreenBottomBorder= new Rectangle(500,90);
        winScreenBottomBorder.setFill(Color.web("#4C1A00"));
        winScreenBottomBorder.setLayoutX(100);
        winScreenBottomBorder.setLayoutY(230);
        winScreenBottomBorder.setArcHeight(30);
        winScreenBottomBorder.setArcWidth(30);
        winScreen.getChildren().add(winScreenBottomBorder);

        Rectangle winScreenBottomPanel = new Rectangle(480,70);
        winScreenBottomPanel.setLayoutX(110);
        winScreenBottomPanel.setLayoutY(240);
        winScreenBottomPanel.setArcHeight(30);
        winScreenBottomPanel.setArcWidth(30);
        winScreenBottomPanel.setFill(Color.WHITE);
        winScreen.getChildren().add(winScreenBottomPanel);

        Text hintComment = new Text();
        hintComment.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        hintComment.setTextAlignment(TextAlignment.CENTER);
        hintComment.setLayoutX(110);
        hintComment.setWrappingWidth(480);
        hintComment.setLayoutY(285);

        if(hintUsed) {
            hintComment.setFill(Color.RED);
            hintComment.setText("(but you used hints)");
        }
        else {
            hintComment.setFill(Color.GREEN);
            hintComment.setText("(without using any hints)");
        }
        winScreen.getChildren().add(hintComment);

        Rectangle restartButton = new Rectangle(200,200);
        restartButton.setArcHeight(30);
        restartButton.setArcWidth(30);
        restartButton.setLayoutX(120);
        restartButton.setLayoutY(350);
        restartButton.setFill(Color.BLANCHEDALMOND);
        Text restartText = new Text("RESTART");
        restartText.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        restartText.setTextAlignment(TextAlignment.CENTER);
        restartText.setFill(Color.web("#4C1A00"));
        restartText.setLayoutX(120);
        restartText.setWrappingWidth(200);
        restartText.setLayoutY(460);
        restartText.setScaleY(3);
        winScreen.getChildren().add(restartButton);
        winScreen.getChildren().add(restartText);

        restartButton.setOnMouseReleased(event -> startGame(hexGame.getPieces().length, numRounds));
        restartText.setOnMouseReleased(event -> startGame(hexGame.getPieces().length, numRounds));

        Rectangle menuButton = new Rectangle(200,200);
        menuButton.setArcHeight(30);
        menuButton.setArcWidth(30);
        menuButton.setLayoutX(380);
        menuButton.setLayoutY(350);
        menuButton.setFill(Color.web("#4C1A00"));
        Text menuText = new Text("MAIN\nMENU");
        menuText.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        menuText.setFill(Color.BLANCHEDALMOND);
        menuText.setTextAlignment(TextAlignment.CENTER);
        menuText.setLayoutX(380);
        menuText.setWrappingWidth(200);
        menuText.setLayoutY(440);
        menuText.setScaleY(2);
        winScreen.getChildren().add(menuButton);
        winScreen.getChildren().add(menuText);

        menuButton.setOnMouseReleased(event -> mainMenu());
        menuText.setOnMouseReleased(event -> mainMenu());

        Animation fireworkDisplay = new Transition() {

            {
                setCycleCount(INDEFINITE);
                setCycleDuration(Duration.millis(1000));
            }
            @Override
            protected void interpolate(double frac) {
                if (random.nextDouble()<0.05) {
                    fireworks.getChildren().add(new Firework(random.nextDouble()*600 +50,random.nextDouble()*600 +50));
                }
                if (fireworks.getChildren().size()>=20) { // Garbage collection
                    fireworks.getChildren().remove(0);
                }
            }
        };
        fireworkDisplay.play();
        winScreen.getChildren().add(fireworks);

        root.getChildren().add(winScreen);

    }

    void mainMenu() {
        PlaySound2();
        root.getChildren().clear();
        pieces.getChildren().clear();
        board.getChildren().clear();
        goal.getChildren().clear();
        UI.getChildren().clear();
        timeDisplay.getChildren().clear();
        moveDisplay.getChildren().clear();
        hintButton.getChildren().clear();
        roundDisplay.getChildren().clear();
        scoreDisplay.getChildren().clear();
        highlights.getChildren().clear();
        ghost.getChildren().clear();
        hint.getChildren().clear();
        goal.getChildren().clear();
        winScreen.getChildren().clear();
        fireworks.getChildren().clear();

        Rectangle background = new Rectangle(700,700,Color.web("#DEB887"));
        root.getChildren().add(background);


        Rectangle startButton = new Rectangle(400,200,Color.LIMEGREEN);
        startButton.setArcHeight(30);
        startButton.setArcWidth(30);
        startButton.setLayoutX(150);
        startButton.setLayoutY(450);
        root.getChildren().add(startButton);

        Text start = new Text("START GAME");
        start.setWrappingWidth(400);
        start.setTextAlignment(TextAlignment.CENTER);
        start.setLayoutX(150);
        start.setLayoutY(570);
        start.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        start.setScaleY(2);
        start.setFill(Color.web("#4C1A00"));
        root.getChildren().add(start);

        startButton.setOnMouseReleased(event -> startGame(numPieces, numRounds));
        start.setOnMouseReleased(event -> startGame(numPieces, numRounds));

        Text title = new Text("COMP1110 Assignment 2\n\"HexGame\"");
        title.setFill(Color.web("4C1A00"));
        title.setWrappingWidth(500);
        title.setLayoutX(100);
        title.setLayoutY(50);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("", FontWeight.BOLD, 40));
        root.getChildren().add(title);

        Text rounds = new Text("Rounds: "+numRounds);
        rounds.setFill(Color.web("#4C1A00"));
        rounds.setFont(Font.font("", FontWeight.BOLD, 40));
        rounds.setLayoutX(50);
        rounds.setLayoutY(340);
        root.getChildren().add(rounds);

        Triangle roundsUp = new Triangle(50);
        roundsUp.setLayoutX(224);
        roundsUp.setLayoutY(290);
        roundsUp.setFill(Color.web("#4C1A00"));
        roundsUp.setOnMouseReleased(event -> {
            if (numRounds < 9) {
                numRounds++;
                rounds.setText("Rounds: " + numRounds);
            }
        });
        root.getChildren().add(roundsUp);

        Triangle roundsDown = new Triangle(50);
        roundsDown.setLayoutX(224);
        roundsDown.setLayoutY(372);
        roundsDown.setRotate(180);
        roundsDown.setFill(Color.web("#4C1A00"));
        roundsDown.setOnMouseReleased(event -> {
            if (numRounds > 3) {
                numRounds--;
                rounds.setText("Rounds: " + numRounds);
            }
        });
        root.getChildren().add(roundsDown);

        Text pieces = new Text("Pieces: "+numPieces);
        pieces.setFill(Color.web("#4C1A00"));
        pieces.setFont(Font.font("", FontWeight.BOLD, 40));
        pieces.setLayoutX(490);
        pieces.setLayoutY(340);
        root.getChildren().add(pieces);

        Triangle piecesUp = new Triangle(50);
        piecesUp.setLayoutX(640);
        piecesUp.setLayoutY(290);
        piecesUp.setFill(Color.web("#4C1A00"));
        piecesUp.setOnMouseReleased(event -> {
            if (numPieces < 4) {
                numPieces++;
                pieces.setText("Pieces: " + numPieces);
            }
        });
        root.getChildren().add(piecesUp);

        Triangle piecesDown = new Triangle(50);
        piecesDown.setLayoutX(640);
        piecesDown.setLayoutY(372);
        piecesDown.setRotate(180);
        piecesDown.setFill(Color.web("#4C1A00"));
        piecesDown.setOnMouseReleased(event -> {
            if (numPieces > 1) {
                numPieces--;
                pieces.setText("Pieces: " + numPieces);
            }
        });
        root.getChildren().add(piecesDown);

        Text tutorialText = new Text("Welcome to HexGame (appologies the main menu is still a work in progress)! Go ahead and start a game. Use the mouse wheel to zoom in and out, the left mouse button to move the board around, and the right mouse button to move pieces. Try to get pieces into the flashing goal in as few moves as possible. Good luck :D");
        tutorialText.setWrappingWidth(500);
        tutorialText.setLayoutY(160);
        tutorialText.setLayoutX(100);
        tutorialText.setFont(Font.font("", 15));
        root.getChildren().add(tutorialText);



    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setScene(scene);
        primaryStage.setTitle("HexGameGUI");
        primaryStage.setResizable(false);
        primaryStage.show();
        numPieces = 4;
        numRounds = 3;
        mainMenu();

    }


    MediaPlayer p;
    void PlaySound1(){ //when the ball moved to the right place
        String name = "assets/button-11.mp3";
        Media sound = new Media(new File(name).toURI().toString());
        p = new MediaPlayer(sound);
        p.setVolume(0.1);
        p.setAutoPlay(true);
    }

    void PlaySound2(){ // when the ball moved to the wrong place
        String name = "assets/button-13.mp3";
        Media sound = new Media(new File(name).toURI().toString());
        p = new MediaPlayer(sound);
        p.setVolume(0.1);
        p.setAutoPlay(true);
    }

    void PlaySound3(){ //when the ball is moving
        String name = "assets/button-14.mp3";
        Media sound = new Media(new File(name).toURI().toString());
        p = new MediaPlayer(sound);
        p.setVolume(0.1);
        p.setAutoPlay(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
