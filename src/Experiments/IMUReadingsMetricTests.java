package Experiments;

import Utility.Vector;
import Webots.IMUReadings;

/**
 * @author Tomasz Darmetko
 */
public class IMUReadingsMetricTests {
    public static void main(String[] args) {

        Vector yAxisVector = new Vector(0, 1, 0);
        Vector otherVector = new Vector(0, 0, 1);

        System.out.println(yAxisVector.crossProduct(otherVector));
        System.out.println(yAxisVector.angleBetween(otherVector));

//        IMUReadings readings1 = IMUReadings.read("./resources/reading1.ser");
//        IMUReadings readings2 = IMUReadings.read("./resources/reading2.ser");
//
//        System.out.println(readings1.getOne(2).getLinear().unitVector());
//        System.out.println(readings2.getOne(2).getLinear().unitVector());
//
//        readings1.toVector().save("./resources/reading1.csv");
//        readings2.toVector().save("./resources/reading2.csv");
//
//        System.out.println(Vector.read("./resources/reading1.csv"));
//        System.out.println(Vector.read("./resources/reading2.csv"));

    }
}
