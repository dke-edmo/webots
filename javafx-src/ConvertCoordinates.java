package Webots;

import Utility.Matrix;
import Utility.RotationVector;
import Utility.Vector;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public class ConvertCoordinates {
    private Point3D point;

    // Takes as input a matrix to rotate coordinate system
    // Rotate such that the point from the initial JavaFX
    // coordinate system to another position thanks to the matrix
    public static Vector covertTo(Matrix matrix, Point3D p){
        double[] pAsVector = {p.getX(), p.getY(), p.getZ()};
        Vector v = new Vector(pAsVector);
        return matrix.multiply(v);
    }

    // to finish
    public static Point3D putInCoordinateSystem(Point3D point){
        // Rotate
        // Translate
        double w = 1000;
        double h = 1000;
        return translateToCenter(new CoordinateSystem(w, h), rotate(point));
    }

    // plot a point Method
    public static Point3D translateToCenter(CoordinateSystem coordinateSystem, Point3D point){
        return new Point3D(point.getX()+(coordinateSystem.getCenter().getX()/2),
                            point.getY()+(coordinateSystem.getCenter().getY()/2),
                                point.getZ());
    }

    // Rotates the point around X axis to get the usual coordinate system
    public static Point3D rotate(Point3D point){
        Vector rotated = Matrix.rotateAroundX(getPointAsVector(point), Math.PI);
        return getVectorAsPoint(rotated);
    }

    public static Vector getPointAsVector(Point3D point){
        double[] p = {point.getX(), point.getY(), point.getZ()};
        return new Vector(p);
    }
    public static RotationVector getPointAsRotationVector(Point3D point){
        double[] p = {point.getX(), point.getY(), point.getZ(), 0};
        return new RotationVector(p);
    }
    public static Point3D getVectorAsPoint(Vector vector){
        return new Point3D(vector.getValue(0), vector.getValue(1), vector.getValue(2));
    }


}
