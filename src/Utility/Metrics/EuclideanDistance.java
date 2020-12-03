package Utility.Metrics;

import Utility.Vector;

public class EuclideanDistance implements Metric {
    public double distance(Vector a, Vector b) {
        return a.euclideanDistance(b);
    }
}
