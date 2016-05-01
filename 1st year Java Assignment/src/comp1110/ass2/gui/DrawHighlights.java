package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Created by X4 on 9/17/2015.
 */
public class DrawHighlights extends Group {
    DrawHighlights(double size, double boardCenterX, double boardCenterY, HexGame hexGame, int turn) {
        super();
        int[] possibleMoves = new int[4];
        int currentPosition = hexGame.getPieces()[turn].getPosition();
        int i = 0;
        for (Direction d : Direction.allDirections) {
            if (SimulateMove.simulateMove(hexGame,turn,d) != currentPosition) {
                possibleMoves[i] = SimulateMove.simulateMove(hexGame,turn,d);
                i++;
            }
        }

        for (int j = 0; j<i; j++) {
            XYcoord coordinate = new XYcoord(possibleMoves[j]);
            Hexagon highlight = new Hexagon((coordinate.x-0.5*coordinate.y)*2*size*0.866+boardCenterX, -coordinate.y*size*1.5+boardCenterY, size*0.95);
            this.getChildren().add(highlight);
            highlight.setFill(Color.YELLOW);
            FadeTransition ft = new FadeTransition(Duration.millis(1000), highlight);
            ft.setFromValue(0.6);
            ft.setToValue(0.0);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();

        }

    }
}
