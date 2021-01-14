package Webots;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;

import java.awt.*;

public class CoordinateSystem {

    private Point3D center;
    private Group root = new Group();
    public CoordinateSystem(double windowWidth, double windowHeight){
        center = new Point3D(windowWidth/2, windowHeight/2, 0);
        root.getChildren().addAll(getAxis(600));
    }
    public Point3D getCenter() {  return center; }
    public Group getGroup() {   return root;    }

    // Later add Scale
    public Cylinder[] getAxis(double scale){
        Point3D x = new Point3D(1, 0, 0);
        Point3D y = new Point3D(0, 1, 0);
        Point3D z = new Point3D(0, 0, 1);

        Cylinder x_axis = connectPoints(new Point3D(0, 0, 0),
                                        new Point3D(scale, 0, 0),
                                        Rotate.Y_AXIS);
        Cylinder y_axis = connectPoints(new Point3D(0, 0, 0),
                new Point3D(0, scale, 0),
                Rotate.Z_AXIS);
        Cylinder z_axis = connectPoints(new Point3D(0, 0, 0),
                new Point3D(0, 0, scale),
                Rotate.X_AXIS);
        Cylinder[] axis = {x_axis, y_axis, z_axis};
        return axis;
    }
    /**
    * @link https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    */
    // Draw a line (as a cylinder) from 2 points
    public Cylinder connectPoints(Point3D start, Point3D end, Point3D axis){
        Point3D difference = end.subtract(start);
        double length = difference.magnitude();

        Point3D midPoint = end.midpoint(start);
        //Translate atMidPoint = new Translate(midPoint.getX(), midPoint.getY(), midPoint.getZ());
        //Translate at = new Translate(length, 0, 0);
        double theta = Math.acos(difference.normalize().dotProduct(axis));
        Rotate rotate = new Rotate(-Math.toDegrees(theta), axis);
        Cylinder positionedAxis = new Cylinder(2, length);
        positionedAxis.getTransforms().addAll(rotate);
        return positionedAxis;
    }
}
