package Utility.Metrics;

import Utility.Vector;

public class LogNormalized implements Metric {

    private Metric metric;

    public LogNormalized(Metric metric) {
        this.metric = metric;
    }

    public double compute(Vector a, Vector b) {
        return metric.compute(a.logNormalize(), b.logNormalize());
    }
    
}
