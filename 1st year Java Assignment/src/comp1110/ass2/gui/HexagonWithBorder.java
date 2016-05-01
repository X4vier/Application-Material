package comp1110.ass2.gui;

import comp1110.ass2.gui.Hexagon;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

/**
 * Created by X4 on 9/11/2015.
 */
public class HexagonWithBorder extends Group {
    public HexagonWithBorder(double centerX, double centerY, double size, double borderThickness, int position) {
        super();
        Hexagon outer = new Hexagon(centerX, centerY, size*1.01);
        outer.setFill(Color.rgb(102, 51, 0));
        this.getChildren().add(outer);
        Hexagon inner = new Hexagon(centerX, centerY, size-borderThickness);
        inner.setFill(Color.web("#DEB887"));
//        inner.setFill(Color.WHITE);
        this.getChildren().add(inner);
//        Text t = new Text();
//        t.setText(""+position);
//        t.setLayoutX(centerX-size/2);
//        t.setLayoutY(centerY);
//        t.setTextAlignment(TextAlignment.CENTER);
//        this.getChildren().add(t);
    }
}
