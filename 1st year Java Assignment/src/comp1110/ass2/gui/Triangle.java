package comp1110.ass2.gui;

import javafx.scene.shape.Polygon;

/**
 * Created by X4 on 10/15/2015.
 */
public class Triangle extends Polygon {
    public Triangle(double size) {
        super();
        this.getPoints().addAll(new Double[]{
                0.0, -(size / 2) * 0.866,
                -0.886 * (size / 2), (size / 2) * 0.5,
                0.886 * (size / 2), (size / 2) * 0.5

        });
    }
}
