package Utility.Metrics;

import Utility.Vector;

public class EuclideanDistance implements Metric {
    public double compute(Vector a, Vector b) {
        return a.euclideanDistance(b);
    }
}
