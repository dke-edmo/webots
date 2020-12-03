package Webots;

import Utility.Serializer;
import Webots.IMUSensor.IMUReading;
import Utility.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IMUReadings implements Serializable {

    static final long serialVersionUID = 1L;

    private List<IMUReading> readings = new ArrayList<>();

    public IMUReadings() {
    }

    public IMUReadings(List<IMUReading> readings) {
        this.readings = readings;
    }

    public List<IMUReading> getAll() {
        return readings;
    }

    public IMUReading getOne(int index) {
        return readings.get(index);
    }

    public IMUReadings getSlice(int from, int to) {
        return new IMUReadings(readings.subList(from, to));
    }

    public void add(IMUReading reading) {
        readings.add(reading);
    }

    public Vector toVector() {
        int counter = 0;
        double[] vector = new double[readings.size() * 6];
        for (IMUReading reading: readings) {
            double[] readingVector = reading.toVector().getDoubleArray();
            for (double v: readingVector) {
                vector[counter] = v;
                counter++;
            }
        }
        return new Vector(vector);
    }

    public void save(String filepath) {
        Serializer.saveToFile(this, filepath);
    }

    public static IMUReadings read(String filepath) {
        return (IMUReadings)Serializer.readFromFile(filepath);
    }

    public String toString() {
        return "IMUReadings{" +
            "readings=" + readings +
            '}';
    }

}
