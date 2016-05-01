package comp1110.ass2.gui;

import comp1110.ass2.Direction;
import comp1110.ass2.HexGame;
import comp1110.ass2.XYcoord;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Created by u5012618 on 7/10/15.
 */
public class HintArrow extends Polygon{

        static final double SCALE_FACTOR = 1.6;

        double angle;

        public HintArrow(double boardCenterX, double boardCenterY, double size, int position, int piece, Direction direction) {
            super();
            this.getPoints().addAll(new Double[]{
                    0.0, -(size*SCALE_FACTOR / 2) * 0.866,
                    -size*SCALE_FACTOR / 3, (size*SCALE_FACTOR / 2) * 0.5,
                    size*SCALE_FACTOR / 3, (size*SCALE_FACTOR / 2) * 0.5

            });
            this.setOpacity(0.8);

            XYcoord coordinate = new XYcoord(position);
            this.setLayoutX((coordinate.x - 0.5 * coordinate.y) * 2 * size * 0.866 + boardCenterX);
            this.setLayoutY(-coordinate.y * size * 1.5 + boardCenterY);

            switch (piece%4) {
                case 0:
                    this.setFill(Color.FORESTGREEN);
                    break;
                case 1:
                    this.setFill(Color.RED);
                    break;
                case 2:
                    this.setFill(Color.YELLOW);
                    break;
                case 3:
                    this.setFill(Color.BLUE);
                    break;
            }

            switch (direction) {
                case UPRIGHT: this.angle = 150;
                    break;
                case RIGHT:this.angle = 90;
                    break;
                case DOWNRIGHT: this.angle=30;
                    break;
                case DOWNLEFT: this.angle=330;
                    break;
                case LEFT: this.angle=270;
                    break;
                case UPLEFT: this.angle=210;
                    break;
            }

            this.getTransforms().add(new Rotate(180-angle, 0, 0));
            TranslateTransition tt = new TranslateTransition(Duration.millis(500), this);
            tt.setByX(size*1.5*Math.sin(angle*Math.PI/180));
            tt.setByY(size*1.5*Math.cos(angle*Math.PI/180));
            tt.setCycleCount(Animation.INDEFINITE);
            tt.setAutoReverse(true);

            tt.play();

        }

}
