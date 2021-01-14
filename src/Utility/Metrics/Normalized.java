package Utility.Metrics;

import Utility.Vector;

public class Normalized implements Metric {

    private Metric metric;

    public Normalized(Metric metric) {
        this.metric = metric;
    }

    public double compute(Vector a, Vector b) {
        return metric.compute(a.normalize(), b.normalize());
    }

}
