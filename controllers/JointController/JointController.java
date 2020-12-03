package JointController;

import Webots.IMUSensor;
import Webots.ObjectCommunicator;
import com.cyberbotics.webots.controller.*;

import static Webots.IMUSensor.*;

/**
 * @author Tomasz Darmetko
 */
public class JointController {

  public static void main(String[] args) {

    Robot robot = new Robot();
    System.out.println("Joint Controller registered for: " + robot.getName());
    int timeStep = (int) Math.round(robot.getBasicTimeStep());

    Motor motor = robot.getMotor("motor");

    ObjectCommunicator<IMUReading, Integer> communicator = new ObjectCommunicator<>(
        robot.getEmitter("emitter"), robot.getReceiver("receiver"), timeStep
    );

    IMUSensor imuSensor = new IMUSensor(robot, timeStep);

    int count = 0;
    double position = 0;
    while (robot.step(timeStep) != -1) {

      communicator.emit(imuSensor.getReading());

      if(communicator.hasNext()) {
        position = (int)communicator.receive();
      }

      motor.setPosition(position);

    }

  }

}
