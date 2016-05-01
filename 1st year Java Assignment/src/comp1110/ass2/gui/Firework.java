package comp1110.ass2.gui;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by X4 on 9/29/2015.
 */
public class Firework extends Group {
    class Particle extends Circle {
        double vx;
        double vy;

        public Particle(double centerX, double centerY, double radius, Color colour) {
            super(centerX, centerY, radius);
            Random r = new Random();
            double velocity = r.nextDouble()*6 + 0;
            double angle = r.nextDouble()*Math.PI*2;
            this.vx = velocity * Math.cos(angle);
            this.vy = velocity * Math.sin(angle);
            this.setFill(colour);
        }
    }

    public Firework(double centerX, double centerY) {
        super();
        Random r = new Random();
        Color c = Color.hsb(r.nextDouble() * 360, 1, 1);

        for (int i = 0; i<500; i++) {
            this.getChildren().add(new Particle(centerX,centerY,3,c));
        }



        Animation explode = new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
                setCycleCount(1);
            }


            protected void interpolate(double frac) {
                for (Node p : Firework.this.getChildren()) {
                    p.setLayoutX(p.getLayoutX() + ((Particle) p).vx);
                    p.setLayoutY(p.getLayoutY() + ((Particle) p).vy);
                    p.setOpacity(p.getOpacity()*0.97);
                }
            }
        };
        explode.play();
        explode.setOnFinished(event -> {
                this.getChildren().clear();
        });
    }
}
