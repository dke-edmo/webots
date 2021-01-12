package Webots;

import Utility.Vector;

import java.io.Serializable;

public class IMUReading implements Serializable {

    static final long serialVersionUID = 1L;

    private final Vector linear;
    private final Vector angular;

    public IMUReading(Vector linear, Vector angular) {
        this.linear = linear;
        this.angular = angular;
    }

    public Vector getLinear() {
        return linear;
    }

    public Vector getAngular() {
        return angular;
    }

    public Vector toVector() {
        return new Vector(
            linear.getDoubleArray(),
            angular.getDoubleArray()
        );
    }

    public String toString() {
        return "IMUReading{" +
            "linear=" + linear +
            ", angular=" + angular +
            '}';
    }

}
