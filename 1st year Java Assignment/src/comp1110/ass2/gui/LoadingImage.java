package comp1110.ass2.gui;

import comp1110.ass2.XYcoord;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by X4 on 9/3/2015.
 */


/*This class exists so we can practice working with JavaFX*/
public class LoadingImage extends Application {

    private static final  String HEXAGON_URI = LoadingImage.class.getResource("assets/smallHexagon.png").toString();


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Loading image into JFX window");
        Group root = new Group();
        Group board = new Group();

        for (int i = 0; i<=216; i++) {
            ImageView hex = new ImageView(HEXAGON_URI);
            XYcoord coordinate = new XYcoord(i);
            int xposition = 450+coordinate.x*44-coordinate.y*22;
            int yposition = 450+coordinate.y*38;
            hex.setLayoutX(xposition);
            hex.setLayoutY(yposition);
            board.getChildren().add(hex);
        }

        root.getChildren().add(board);



        Scene scene = new Scene(root, 900, 900);

        double initialX;
        double initialY;



        scene.setOnMouseDragged(event1 -> {
            board.setLayoutX(board.getLayoutX() - 0.01*event1.getX());
            board.setLayoutY(board.getLayoutY() - 0.01*event1.getY());
        });



        scene.setOnScroll(event -> {
            if (event.getDeltaY() >= 0) {
                board.setScaleX(board.getScaleX() * 1.1);
                board.setScaleY(board.getScaleY() * 1.1);
            }
            if (event.getDeltaY() <= 0) {
                board.setScaleX(board.getScaleX()/1.1);
                board.setScaleY(board.getScaleY()/1.1);
            }
        });

            primaryStage.setScene(scene);
            primaryStage.show();

        }

    public static void main(String[] args) {
        launch(args);
    }
}
