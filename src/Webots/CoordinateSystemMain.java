package Webots;

import Utility.Matrix;
import Utility.Vector;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class CoordinateSystemMain extends Application{

    final static double WIDTH = 1000;
    final static double HEIGHT = 800;
    public static void main(String[] args){
        Application.launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("My First JavaFX App");

        Slider slider = new Slider(0, 360, 0);
        slider.setBlockIncrement(1);
        slider.setTranslateX(WIDTH/2);
        slider.setTranslateY(HEIGHT-100);
        CoordinateSystem cs = new CoordinateSystem(WIDTH, HEIGHT);
        Group a = new Group(cs.getGroup());

        Group rotationGroup = new Group(a);
        rotationGroup.setRotationAxis(Rotate.Y_AXIS);
        rotationGroup.rotateProperty().bind(slider.valueProperty());
        rotationGroup.setTranslateX(cs.getCenter().getX());
        rotationGroup.setTranslateY(cs.getCenter().getY());
        rotationGroup.setDepthTest(DepthTest.ENABLE);

        Group root = new Group(rotationGroup, slider);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);


        Color c = Color.rgb(40, 50, 80);
        Scene scene = new Scene(root, WIDTH, HEIGHT, c);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
