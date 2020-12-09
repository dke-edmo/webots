import Webots.IMUSensor;
import Webots.ObjectCommunicator;
import com.cyberbotics.webots.controller.*;

import java.util.Random;

import static Webots.IMUSensor.*;

/**
 * @author Tomasz Darmetko
 */
public class JointController {

    public static void main(String[] args) {

        Robot robot = new Robot();
        System.out.println("Joint Controller registered for: " + robot.getName());

        int robot_index = Integer.parseInt(robot.getName().split("t")[1]);
        System.out.println(robot.getName() + " has index " + robot_index);

        int timeStep = (int) Math.round(robot.getBasicTimeStep());

        Motor motor = robot.getMotor("motor");

        ObjectCommunicator<IMUReading, Double> communicator = new ObjectCommunicator<>(
            robot.getEmitter("emitter"), robot.getReceiver("receiver"), timeStep
        );

        IMUSensor imuSensor = new IMUSensor(robot, timeStep);

        double count = 0;
        double position = 0;
        while (robot.step(timeStep) != -1) {

            communicator.emit(imuSensor.getReading());

            if (communicator.hasNext()) {
                position = communicator.receive();
            }

           /* Random rand = new Random();
            position = rand.nextDouble();*/

            position = count;
            count += 0.01;

            if (count >= 1){
                count = 0;
            }

            motor.setPosition(position);

        }

    }

}
