package comp1110.ass2.gui;

import comp1110.ass2.*;
import comp1110.ass2.Cranny;
import comp1110.ass2.Nook;
import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 * Created by X4 on 9/11/2015.
 */
public class DrawBoard extends Group {
    DrawBoard(HexGame hexGame, double size, double centerX, double centerY) {
        super();
        for (int i = 169; i<=216; i++) { // Creating the border
            XYcoord coordinate = new XYcoord(i);
            Hexagon hex = new Hexagon((coordinate.x-0.5*coordinate.y)*2*size*0.866+centerX,-coordinate.y*size*1.5+centerY,size*1.3);
            hex.setFill(Color.rgb(51, 25, 0));
            this.getChildren().add(hex);
        }

        for (int i = 0; i<=216; i++) { // Creating the hexagons which make up the board
            XYcoord coordinate = new XYcoord(i);
            this.getChildren().add(new HexagonWithBorder((coordinate.x-0.5*coordinate.y)*2*size*0.866+centerX,-coordinate.y*size*1.5+centerY,size,0.05*size,i));
        }

        for (Nook n: hexGame.getNooks()) {
            XYcoord coordinate = new XYcoord(n.getPosition());
            comp1110.ass2.gui.Nook nook = new comp1110.ass2.gui.Nook(n.getDirection(), size, size*0.2, (coordinate.x-0.5*coordinate.y)*2*size*0.866+centerX, -coordinate.y*size*1.5+centerY);
            this.getChildren().add(nook);
        }

        for (Cranny c: hexGame.getCrannies()) {
            XYcoord coordinate = new XYcoord(c.getPosition());
            comp1110.ass2.gui.Cranny cranny = new comp1110.ass2.gui.Cranny(c.getPosition(), size, size*0.2, (coordinate.x-0.5*coordinate.y)*2*size*0.866+centerX, -coordinate.y*size*1.5+centerY);
            this.getChildren().add(cranny);
        }


    }
}
