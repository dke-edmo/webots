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

}
