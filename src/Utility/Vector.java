package Utility;

import Webots.IMUReadings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Vector implements Serializable {

    static final long serialVersionUID = 1L;

    private final double[] vector;

    public Vector(int size) {
        if(size <= 0) throw new RuntimeException("Vector must not be empty!");
        this.vector = new double[size];
    }

    public Vector(double ...vector) {
        if(vector.length <= 0) throw new RuntimeException("Vector must not be empty!");
        this.vector = vector;
        validate();
    }

    public Vector(double[] ...vectors) {
        if(vectors.length <= 0) throw new RuntimeException("Vector must not be empty!");
        int size = Arrays.stream(vectors).mapToInt(v -> v.length).sum();
        vector = new double[size];
        int counter = 0;
        for (double[] aVector: vectors) {
            for (double value: aVector) {
                vector[counter++] = value;
            }
        }
        validate();
    }

    public Vector(List<Double> vecList) {
        vector = new double[vecList.size()];
        for (int i = 0; i < vecList.size(); i++) {
            vector[i] = vecList.get(i);
        }
        validate();
    }

    private void validate() {
        for (int i = 0; i < vector.length; i++) {
            Require.realNumber(vector[i], "The vector " + i + " value must be real.");
        }
    }

    public double getValue(int index) {
        return vector[index];
    }

    public Vector slice(int from, int to) {
        return new Vector(Arrays.copyOfRange(vector, from, to + 1));
    }

    public Matrix asMatrix(int rows, int columns) {
        if(rows * columns != getSize()) throw new RuntimeException("Wrong rows and columns size!");
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = vector[i * columns + j];
            }
        }
        return new Matrix(matrix);
    }

    public Matrix asRowMatrix() {
        return asMatrix(1, getSize());
    }

    public Matrix asColumnMatrix() {
        return asMatrix(getSize(), 1);
    }

    public Vector getEveryNthValue(int n, int startingFrom) {
        List<Double> newVector = new ArrayList<>();
        for (int i = startingFrom; i < getSize(); i += n) {
            newVector.add(vector[i]);
        }
        return new Vector(newVector);
    }

    public double[] getDoubleArray() {
        return vector;
    }

    public Vector map(DoubleUnaryOperator mapper) {
        return new Vector(getStream().map(mapper).toArray());
    }

    public double sumAll() {
        return getStream().sum();
    }

    public double max() {
        return getStream().max().getAsDouble();
    }

    public double min() {
        return getStream().min().getAsDouble();
    }

    public double mean() {
        return sumAll() / getSize();
    }

    public double length() {
        return Math.sqrt(square().sumAll());
    }

    public Vector unitVector() {
        Require.notZero(length(), "Unit vector of 0 length vector is undefined.");
        return divide(length());
    }

    public Vector add(double scalar) {
        return map(v -> v + scalar);
    }

    public Vector subtract(double scalar) {
        return map(v -> v - scalar);
    }

    public Vector multiply(double scalar) {
        return map(v -> v * scalar);
    }

    public Vector divide(double scalar) {
        return map(v -> v / scalar);
    }

    public Vector add(Vector other) {
        double[] newVector = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            newVector[i] = vector[i] + other.vector[i];
        }
        return new Vector(newVector);
    }
    public Vector subtract(Vector other) {
        double[] newVector = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            newVector[i] = vector[i] - other.vector[i];
        }
        return new Vector(newVector);
    }

    public Vector multiply(Vector other) {
        double[] newVector = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            newVector[i] = vector[i] * other.vector[i];
        }
        return new Vector(newVector);
    }

    public Vector crossProduct(Vector other) {
        if(this.getSize() != 3 || this.getSize() != other.getSize()) {
            throw new RuntimeException(
                "Both vectors must be 3D for cross product! " + this.getSize() + " vs. " + other.getSize()
            );
        }
        return new Vector(
            vector[1]*other.vector[2] - vector[2]*other.vector[1],
            vector[2]*other.vector[0] - vector[0]*other.vector[2],
            vector[0]*other.vector[1] - vector[1]*other.vector[0]
        );
    }

    /**
     * Get rotation vector that rotates this vector to the other vector.
     * @link https://en.wikipedia.org/wiki/Rotation_formalisms_in_three_dimensions#Euler_axis_and_angle_(rotation_vector)
     */
    public RotationVector rotationVector(Vector other) {
        try {
            Require.positive(this.length(), "This vector has no direction!");
            Require.positive(other.length(), "Other vector has no direction!");
            // the rotation axis in underspecified if both vectors have the same direction
            Vector axisVector;
            if(this.crossProduct(other).length() != 0) {
                axisVector = this.crossProduct(other);
            } else {
                double a = vector[0], b = vector[1], c = vector[2];
                System.out.println("- (a + b) / c" + - (a + b) / c);
                axisVector = new Vector(1, 1, - (a + b) / c);
                System.out.println(this.dotProduct(axisVector));
            }
            Vector angleVector = new Vector(this.angleBetween(other));
            return new RotationVector(
                axisVector.unitVector().getDoubleArray(),
                angleVector.getDoubleArray()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                "Exception thrown when computing rotation vector between:\n" +
                "This: " + this + "\n" +
                "Other: " + other + "\n",
                e
            );
        }
    }

    public double dotProduct(Vector other) {
        return multiply(other).sumAll();
    }

    public double angleBetween(Vector other) {
        return Math.acos(
            cosineSimilarity(other)
        );
    }

    public Vector square() {
        return map(v -> v * v);
    }

    public Vector log() {
        return map(Math::log);
    }

    /**
     * Makes sure that there is no log of a negative value before normalization.
     * @link https://www.researchgate.net/post/How-can-I-log-transform-a-series-with-both-positive-and-negative-values
     */
    public Vector logNormalize() {
        return add(1 - min()).log().normalize();
    }

    public Vector normalize() {
        double min = min();
        return map(v -> v - min).divide(max() - min);
    }

    public double euclideanDistance(Vector other) {
        return this.subtract(other).length();
    }

    public double cosineSimilarity(Vector other) {
        Require.notZero(length(), "Cosine similarity of 0 length vector is undefined (this).");
        Require.notZero(other.length(), "Cosine similarity of 0 length vector is undefined (other).");
        return Math.max(Math.min(dotProduct(other) / (length() * other.length()), 1), -1);
    }

    public boolean isEqualSize(Vector other) {
        return this.getSize() == other.getSize();
    }

    public int getSize() {
        return vector.length;
    }

    public DoubleStream getStream() {
        return Arrays.stream(vector);
    }

    public void save(String filepath) {
        CSV.saveToFile(this, filepath);
    }

    public static Vector read(String filepath) {
        return CSV.readFromFile(filepath);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vector(");
        for(double value: vector) {
            builder.append(Utils.round(value));
            builder.append(", ");
        }
        builder.append(")");
        return builder.toString();
    }
}
