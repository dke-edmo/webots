package Utility;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CSV {

    public static void saveToFile(Vector vector, String filepath) {
        try (FileWriter file = new FileWriter(filepath)) {
            file.write(
                vector
                    .getStream()
                    .mapToObj(Double::toString)
                    .collect(Collectors.joining(", "))
            );
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Vector readFromFile(String filepath) {
        try (BufferedReader file = new BufferedReader(new FileReader(filepath))) {
            double[] doubleArray = Arrays
                .stream(file.readLine().split(","))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
            return new Vector(doubleArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
