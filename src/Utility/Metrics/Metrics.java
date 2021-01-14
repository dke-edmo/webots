package Utility.Metrics;

import Utility.Pair;
import Utility.Utils;
import Utility.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {
    List<Pair<Metric, List<Double>>> metrics = new ArrayList<>();

    public Metrics() {

        metrics.add(new Pair<Metric, List<Double>>(new CosineSimilarity(), new ArrayList<>()));
        metrics.add(new Pair<Metric, List<Double>>(new Normalized(new CosineSimilarity()), new ArrayList<>()));
//        metrics.add(new Pair<Metric, List<Double>>(new LogNormalized(new CosineSimilarity()), new ArrayList<>()));

        metrics.add(new Pair<Metric, List<Double>>(new EuclideanDistance(), new ArrayList<>()));
        metrics.add(new Pair<Metric, List<Double>>(new Normalized(new EuclideanDistance()), new ArrayList<>()));
//        metrics.add(new Pair<Metric, List<Double>>(new LogNormalized(new EuclideanDistance()), new ArrayList<>()));

    }


    public void addResult(Vector a, Vector b) {
        for (Pair<Metric, List<Double>> entry: metrics) {
            // compute metrics
            entry.getRight().add(entry.getLeft().compute(a, b));
        }
    }

    public void print() {
        for (Pair<Metric, List<Double>> entry: metrics) {
            System.out.print(Utils.round(entry.getRight().stream().mapToDouble(v -> v).average().getAsDouble()));
            System.out.print(", ");
        }
    }

}
