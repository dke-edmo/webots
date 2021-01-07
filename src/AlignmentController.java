import Utility.Vector;
import Webots.IMUSensor;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.Field;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Supervisor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        WebotsNode root = new WebotsNode(supervisor.getRoot());

//        root.importChildFromString(
//            FileSystem
//                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
//                .replaceAll("__id__", "1")
//        );

        ObjectCommunicator<Double, IMUSensor.IMUReading> communicator1 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter1"), supervisor.getReceiver("receiver1"), timeStep
        );

        ObjectCommunicator<Double, IMUSensor.IMUReading> communicator2 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter2"), supervisor.getReceiver("receiver2"), timeStep
        );

        WebotsNode robot1 = new WebotsNode(supervisor.getFromDef("robot1"));
        WebotsNode robot2 = new WebotsNode(supervisor.getFromDef("robot2"));

        Node cRobot1 = getConnectors(supervisor, robot1).get(3);
        Node cRobot2 = getConnectors(supervisor, robot2).get(3);

        System.out.println(cRobot1);
        System.out.println(cRobot2);

        robot1.setRotation(new Vector(0, 0, 1, Math.PI));
        robot2.setRotation(new Vector(0, 0, 1, 0));

        Vector connectorRelativeToRobot1 = robot1.getPosition().subtract(new Vector(cRobot1.getPosition()));
        System.out.println("C1 displacement from R1" + connectorRelativeToRobot1);

        robot1.setPosition(new Vector(cRobot2.getPosition()).subtract(connectorRelativeToRobot1));

        while (supervisor.step(timeStep) != -1) {
        }

    }

    public static List<Node> getConnectors(Supervisor supervisor, WebotsNode robot) {
        return SupervisorController.getConnectors(robot);
    }

}
