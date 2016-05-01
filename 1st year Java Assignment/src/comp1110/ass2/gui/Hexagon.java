package comp1110.ass2.gui;

import javafx.scene.shape.Polygon;

/**
 * Created by X4 on 9/11/2015.
 */
public class Hexagon extends Polygon {
    double centerX;
    double centerY;
    public Hexagon(double centerX, double centerY, double size) {
        super();
        super.getPoints().addAll((new Double[]{
                0.0, 0.0,
                0.0, size,
                0.866*size, 1.5*size,
                2*0.866*size, size,
                2*0.866*size, 0.0,
                0.866*size, -0.5*size}));

        this.centerX = centerX;
        this.centerY = centerY;

        this.setLayoutX(centerX - 0.866*size);
        this.setLayoutY(centerY - 0.5 * size);
    }
}