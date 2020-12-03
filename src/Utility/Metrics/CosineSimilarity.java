package Utility.Metrics;

import Utility.Vector;

public class CosineSimilarity implements Metric {
    public double distance(Vector a, Vector b) {
        return a.cosineSimilarity(b);
    }
}
