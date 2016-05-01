package comp1110.ass2.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * Created by X4 on 9/12/2015.
 */
public class Cranny extends Polygon {
    public Cranny(int position, double size, double thickness, double centerX, double centerY) {
        super();
        super.getPoints().addAll((new Double[]{

                0.0, 0.0,
                0.0, size + thickness,
                thickness, size + thickness,
                thickness, 0.0,
        }));

        this.setFill(Color.rgb(51,25,0));

        this.setLayoutX(centerX-0.866*size-thickness/2);
        this.setLayoutY(centerY - 0.5*size);
        switch ((position-169)/8) {
            case 0: this.getTransforms().add(new Rotate(180,0.866*size+thickness/2,0.5*size));
                break;
            case 1: this.getTransforms().add(new Rotate(240,0.866*size+thickness/2,0.5*size));
                break;
            case 2: this.getTransforms().add(new Rotate(300,0.866*size+thickness/2,0.5*size));
                break;
            case 3: this.getTransforms().add(new Rotate(360,0.866*size+thickness/2,0.5*size));
                break;
            case 4: this.getTransforms().add(new Rotate(60,0.866*size+thickness/2,0.5*size));
                break;
            case 5: this.getTransforms().add(new Rotate(120,0.866*size+thickness/2,0.5*size));
                break;
        }

    }
}
