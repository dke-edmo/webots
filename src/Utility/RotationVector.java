package Utility;

public class RotationVector extends Vector {

    public RotationVector() {
        super(0, 1, 0, 0);
    }

    public RotationVector(double... vector) {
        super(vector);
        if(this.getSize() != 4) throw new RuntimeException("Rotation vector must be of size 4!");
    }

    public RotationVector(double[]... vectors) {
        super(vectors);
    }

    public RotationVector(Vector axisVector, double angle) {
        super(
            axisVector.unitVector().getDoubleArray(),
            new Vector(angle).getDoubleArray()
        );
    }

    public RotationVector addRotation(RotationVector vector) {
        Matrix firstRotation = this.rotationMatrix();
        Matrix secondRotation = vector.rotationMatrix();
        Matrix sumOfRotations = firstRotation.multiply(secondRotation);
        Vector yAxisVector = new Vector(0, 1, 0);
        Vector rotatedYAxisVector = sumOfRotations.multiply(yAxisVector);
        return yAxisVector.rotationVector(rotatedYAxisVector);
    }

    /**
     * @link https://people.eecs.berkeley.edu/~ug/slide/pipeline/assignments/as5/rotation.html
     */
    public Matrix rotationMatrix() {
        Vector rotationAxis = this.slice(0, 2);
        double rotationAngle = this.getValue(3);
        // I + A sin(b) + A2 [1 - cos(b)]
        Matrix A = Matrix.getCrossProductMatrix(rotationAxis);
        Matrix A2 = A.multiply(A);
        return Matrix.getIdentityMatrix(3)
            .add(A.multiply(Math.sin(rotationAngle)))
            .add(A2.multiply(1 - Math.cos(rotationAngle)));
    }

}
