package Utility;

import Webots.IMUReadings;

import java.io.Serializable;
import java.util.Arrays;
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
    }

    public double getValue(int index) {
        return vector[index];
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
            throw new RuntimeException("Both vectors must be 3D for cross product!");
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
    public Vector rotationVector(Vector other) {
        Vector axisVector = this.crossProduct(other).unitVector();
        Vector angleVector = new Vector(this.angleBetween(other));
        return new Vector(axisVector.getDoubleArray(), angleVector.getDoubleArray());
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
        return dotProduct(other) / (length() * other.length());
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
