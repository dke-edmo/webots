package Utility;

import java.util.Arrays;

public class Matrix {

    private final int rows;
    private final int columns;
    private double[][] matrix; // rows-by-columns

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix = new double[rows][columns];
    }

    public Matrix(double[][] matrix) {
        rows = matrix.length;
        columns = matrix[0].length;
        this.matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }


    public static Matrix getIdentityMatrix(int size) {
        Matrix identityMatrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            identityMatrix.matrix[i][i] = 1;
        }
        return identityMatrix;
    }

    public static Matrix getCrossProductMatrix(Vector vector) {
        if(vector.getSize() != 3) throw new RuntimeException("Cross product is defined only in 3 dimensions!");
        double x, y, z;
        x = vector.getValue(0);
        y = vector.getValue(1);
        z = vector.getValue(2);
        return new Matrix(new double[][]{
            {0, -z, y},
            {z, 0, -x},
            {-y, x, 0}
        });
    }
    public double[][] toDoubleArray()
    {
        return matrix;
    }

    /**
     * Source: https://rosettacode.org/wiki/Gauss-Jordan_matrix_inversion#Java
     */
    public Matrix inverse() {
        assert(rows == columns);
        // Augment by identity matrix
        Matrix tmp = new Matrix(rows, columns * 2);
        for (int row = 0; row < rows; ++row) {
            System.arraycopy(matrix[row], 0, tmp.matrix[row], 0, columns);
            tmp.matrix[row][row + columns] = 1;
        }
        tmp.toReducedRowEchelonForm();
        Matrix inv = new Matrix(rows, columns);
        for (int row = 0; row < rows; ++row)
            System.arraycopy(tmp.matrix[row], columns, inv.matrix[row], 0, columns);
        return inv;
    }

    /**
     * Source: https://rosettacode.org/wiki/Gauss-Jordan_matrix_inversion#Java
     */
    private void toReducedRowEchelonForm() {
        for (int row = 0, lead = 0; row < rows && lead < columns; ++row, ++lead) {
            int i = row;
            while (matrix[i][lead] == 0) {
                if (++i == rows) {
                    i = row;
                    if (++lead == columns)
                        return;
                }
            }
            swapRows(i, row);
            if (matrix[row][lead] != 0) {
                double f = matrix[row][lead];
                for (int column = 0; column < columns; ++column)
                    matrix[row][column] /= f;
            }
            for (int j = 0; j < rows; ++j) {
                if (j == row)
                    continue;
                double f = matrix[j][lead];
                for (int column = 0; column < columns; ++column)
                    matrix[j][column] -= f * matrix[row][column];
            }
        }
    }

    /**
     * Source: https://rosettacode.org/wiki/Gauss-Jordan_matrix_inversion#Java
     */
    private void swapRows(int i, int j) {
        double[] tmp = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = tmp;
    }

    public Matrix add(Matrix B) {
        Matrix A = this;
        if (B.rows != A.rows || B.columns != A.columns) {
            throw new RuntimeException("Matrices must match in dimensions!");
        }
        Matrix C = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C.matrix[i][j] = A.matrix[i][j] + B.matrix[i][j];
            }
        }
        return C;
    }

    public Matrix subtract(Matrix B) {
        return add(B.multiply(-1));
    }

    public Matrix multiply(double scalar) {
        Matrix A = this;
        Matrix C = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C.matrix[i][j] = A.matrix[i][j] * scalar;
            }
        }
        return C;
    }

    public Vector multiply(Vector vector) {
        Matrix A = this;
        if (A.columns != vector.getSize()) {
            throw new RuntimeException("A matrix columns does not vector size!");
        }
        return multiply(vector.asColumnMatrix()).getColumn(0);
    }

    public Matrix multiply(Matrix B) {
        Matrix A = this;
        if (A.columns != B.rows) {
            throw new RuntimeException(
                "A matrix columns: " + A.columns + " does not match B matrix rows: " + B.rows + "!"
            );
        }
        Matrix C = new Matrix(A.rows, B.columns);
        for (int i = 0; i < C.rows; i++) {
            for (int j = 0; j < C.columns; j++) {
                for (int k = 0; k < A.columns; k++) {
                    C.matrix[i][j] += (A.matrix[i][k] * B.matrix[k][j]);
                }
            }
        }
        return C;
    }

    public Vector getRow(int i) {
        return new Vector(matrix[i]);
    }

    public Vector getColumn(int i) {
        double[] column = new double[rows];
        for (int j = 0; j < rows; j++) {
            column[j] = matrix[j][i];
        }
        return new Vector(column);
    }

    public static Vector rotateAroundX(Vector vector, double theta){
        return get3DRotationX(theta).multiply(vector);
    }
    public static Vector rotateAroundY(Vector vector, double theta){
        return get3DRotationY(theta).multiply(vector);
    }
    public static Vector rotateAroundZ(Vector vector, double theta){
        return get3DRotationZ(theta).multiply(vector);
    }
    // Counter-ClockWise rotation
    private static Matrix get3DRotationX(double theta){
        double[][] matrix = {{1, 0, 0},
                            {0, Math.cos(theta), -Math.sin(theta)},
                            {0, Math.sin(theta), Math.cos(theta)}};
        return new Matrix(matrix);
    }
    private static Matrix get3DRotationY(double theta){
        double[][] matrix = {{Math.cos(theta), 0, Math.sin(theta)},
                            {0, 1, 0},
                            {-Math.sin(theta), 0, Math.cos(theta)}};
        return new Matrix(matrix);
    }
    private static Matrix get3DRotationZ(double theta){
        double[][] matrix = {{Math.cos(theta), -Math.sin(theta), 0},
                            {Math.sin(theta), Math.cos(theta), 0},
                            {0, 0, 1}};
        return new Matrix(matrix);
    }

    public double getTrace() {
        if (this.rows != this.columns) throw new RuntimeException("Trace can be computed only for square matrix");
        double trace = 0;
        for (int i = 0; i < this.rows; i++) {
            trace += matrix[i][i];
        }
        return trace;
    }

    public RotationVector getRotationVector() {
        if (this.rows != 3 || this.columns != 3) throw new RuntimeException("Rotation matrix must be 3x3!");
        Vector axisVector = new Vector(
            matrix[2][1] - matrix[1][2],
            matrix[0][2] - matrix[2][0],
            matrix[1][0] - matrix[0][1]
        );
        if(axisVector.length() == 0) return new RotationVector(0, 1, 0, 0);
        return new RotationVector(
            axisVector,
            Math.atan2(axisVector.length(), getTrace() - 1)
        );
    }
    public Vector asVector()
    {
        double[] vector = new double[matrix.length * matrix[0].length];
        for(int i=0; i<vector.length; i++)
        {
            vector[i] = matrix[i / columns][i % columns];
        }
        return new Vector(vector);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Matrix{" +
            "rows=" + rows +
            ", columns=" + columns +
            "} =\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                builder.append(Utils.round(matrix[i][j]));
                builder.append("\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
