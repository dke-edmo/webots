package Experiments;

import Utility.Matrix;
import Utility.RotationVector;

public class GeometryTest {
    public static void main(String[] args) {
        RotationVector vec = new RotationVector(0, 0, 1, Math.PI);
        System.out.println(vec.rotationMatrix());
        System.out.println(
            vec.rotationMatrix().getRotationVector()
        );

        Matrix findOpposite = Matrix.getIdentityMatrix(3).multiply(-1);
        System.out.println(findOpposite.getRotationVector());
    }
}
