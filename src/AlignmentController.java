import Utility.FileSystem;
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

        root.importChildFromString(
            FileSystem
                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                .replaceAll("__id__", "1")
        );

        root.importChildFromString(
            FileSystem
                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                .replaceAll("__id__", "2")
        );

        ObjectCommunicator<Double, IMUSensor.IMUReading> communicator1 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter1"), supervisor.getReceiver("receiver1"), timeStep
        );

        ObjectCommunicator<Double, IMUSensor.IMUReading> communicator2 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter2"), supervisor.getReceiver("receiver2"), timeStep
        );

        WebotsNode edmo1 = new WebotsNode(supervisor.getFromDef("edmo1"));
        WebotsNode edmo2 = new WebotsNode(supervisor.getFromDef("edmo2"));

        Node connectorEdmo1 = SupervisorController.getConnectors(edmo1).get(3);
        Node connectorEdmo2 = SupervisorController.getConnectors(edmo2).get(3);

        edmo1.setRotation(new Vector(0, 0, 1, Math.PI));
        edmo2.setRotation(new Vector(0, 0, 1, 0));

        Vector connectorRelativeToRobot1 = edmo1.getPosition().subtract(new Vector(connectorEdmo1.getPosition()));
        edmo1.setPosition(new Vector(connectorEdmo2.getPosition()).subtract(connectorRelativeToRobot1));

        while (supervisor.step(timeStep) != -1) {
        }

    }

}
