package Webots;

import Utility.Vector;
import com.cyberbotics.webots.controller.Accelerometer;
import com.cyberbotics.webots.controller.Gyro;
import com.cyberbotics.webots.controller.Robot;

import java.io.Serializable;

/**
 * Simplifies management of an IMU sensor.
 *
 * @author Tomasz Darmetko
 */
public class IMUSensor {

    private final Accelerometer accelerometer;
    private final Gyro gyro;

    public IMUSensor(Robot robot, int timeStep) {
        this(
            robot.getAccelerometer("accelerometer"),
            robot.getGyro("gyro"),
            timeStep
        );
    }

    public IMUSensor(Accelerometer accelerometer, Gyro gyro, int timeStep) {
        this.accelerometer = accelerometer;
        accelerometer.enable(timeStep);
        this.gyro = gyro;
        gyro.enable(timeStep);
    }

    public IMUReading getReading() {
        return new IMUReading(
            new Vector(accelerometer.getValues()),
            new Vector(gyro.getValues())
        );
    }

    public static class IMUReading implements Serializable {

        static final long serialVersionUID = 1L;

        private final Vector linear;
        private final Vector angular;

        public IMUReading(Vector linear, Vector angular) {
            this.linear = linear;
            this.angular = angular;
        }

        public Vector getLinear() {
            return linear;
        }

        public Vector getAngular() {
            return angular;
        }

        public Vector toVector() {
            return new Vector(
                linear.getDoubleArray(),
                angular.getDoubleArray()
            );
        }

        public String toString() {
            return "IMUReading{" +
                "linear=" + linear +
                ", angular=" + angular +
                '}';
        }

    }

}
