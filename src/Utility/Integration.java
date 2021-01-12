package Utility;

import Webots.IMUReadings;
import Webots.IMUReading;

import java.util.ArrayList;
import java.util.List;


public class Integration {

    public static void main(String[] args) {
        //Test with constant acceleration
        List<IMUReading> name = new ArrayList<>();
        double acc = 2;
        for (int i = 0; i < 9; i++) {
            Vector accs = new Vector(acc, acc, acc);
            name.add(new IMUReading(accs, accs));
        }
        //Vector displacement = getDisplacement(IMUReadings.read("./resources/reading1.ser"));
        Vector displacement = getDisplacementHistory(new IMUReadings(name));
        System.out.println(displacement.toString());
    }

    public static Vector getDisplacementHistory(IMUReadings readings) {
//        Supervisor supervisor = new Supervisor();
//        double timeStep =  supervisor.getBasicTimeStep();
        double timeStep = 1;

        Vector history = new Vector(0, 0, 0);
        Vector displacement = new Vector(0, 0, 0);
        Vector velocity = new Vector(0, 0, 0);  //Must start sith no velocity

        Vector initial = readings.getOne(0).getLinear();
        //Calculates displacement between each reading
        for (IMUReading reading : readings.getFrom(1)) {
            Vector next = reading.getLinear();
            displacement = displacement.add(findDisplacementVector(initial, next, velocity, timeStep));
            history = history.append(displacement);
            velocity = velocity.add(initial.add(next).multiply(timeStep / 2));  //Calculates new velocity by finding area under the curve
            initial = next;
        }
        return history;
    }


    public static Vector findDisplacementVector(Vector acc1, Vector acc2, Vector velocity, double timeStep) {
        double[] disp = new double[acc1.getSize()];
        for (int i = 0; i < acc1.getSize(); i++) {
            disp[i] = findDisplacement(acc1.getValue(i), acc2.getValue(i), velocity.getValue(i), timeStep);
        }
        return new Vector(disp);
    }

    public static double findDisplacement(double acc1, double acc2, double velocity, double timeStep) {
        double a = (acc2 - acc1) / timeStep;  // find a value for ax +b
        return (a / 6) * Math.pow(timeStep, 3) + (acc1 / 2) * Math.pow(timeStep, 2) + velocity*timeStep;
    }
}
