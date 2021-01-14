package Utility.Metrics;

import Utility.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Metric {
    double compute(Vector a, Vector b);
}
