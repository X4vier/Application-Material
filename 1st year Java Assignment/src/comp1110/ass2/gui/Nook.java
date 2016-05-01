package comp1110.ass2.gui;

import comp1110.ass2.Direction;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * Created by X4 on 9/11/2015.
 */
public class Nook extends Polygon {
    public Nook(Direction direction, double size, double thickness, double centerX, double centerY) {
        super();
        double size2 = size - thickness/0.866;
        super.getPoints().addAll((new Double[]{

                -thickness/2, 0.0,
                -thickness/2, size,
                0.866 * size, 1.5 * size,
                0.866 * size, 1.5 * size - thickness / 0.866,
                0.866 * size - 0.866 * size2 -thickness*0.866/2, 1.5 * size - thickness / 0.866 - 0.5 * size2,
                0.866 * size - 0.866 * size2 -thickness*0.866/2, 1.5 * size - thickness / 0.866 - 1.5 * size2,
                0.866 * size, 1.5 * size - thickness / 0.866 - 2 * size2,

                0.866 * size, -0.5 * size
        }));
        this.setLayoutX(centerX - 0.866 * size);
        this.setLayoutY(centerY - 0.5 * size);

        this.setFill(Color.rgb(51, 25, 0));

        switch(direction) {
            case RIGHT: break;
            case DOWNRIGHT: this.getTransforms().add(new Rotate(60,0.866*size,0.5*size));
                break;
            case DOWNLEFT: this.getTransforms().add(new Rotate(120,0.866*size,0.5*size));
                break;
            case LEFT: this.getTransforms().add(new Rotate(180,0.866*size,0.5*size));
                break;
            case UPLEFT: this.getTransforms().add(new Rotate(240,0.866*size,0.5*size));
                break;
            case UPRIGHT: this.getTransforms().add(new Rotate(300,0.866*size,0.5*size));
                break;
        }


    }


}
