import Utility.FileSystem;
import Utility.RotationVector;
import Utility.Vector;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.Supervisor;

import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        WebotsNode root = new WebotsNode(supervisor.getRoot());

        Webots.Supervisor.setSupervisor(supervisor);

        supervisor.step(timeStep);
        supervisor.simulationResetPhysics();

//        importEdmos(supervisor, root);
//        connect(
//            supervisor, timeStep,
//            new WebotsNode(supervisor.getFromDef("edmo1")),
//            new WebotsNode(supervisor.getFromDef("edmo2")),
//            1, 1
//        );

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + " " + j);
                importEdmos(supervisor, root);
                connect(
                    supervisor, timeStep,
                    new WebotsNode(supervisor.getFromDef("edmo1")),
                    new WebotsNode(supervisor.getFromDef("edmo2")),
                    i, j
                );
                for (int k = 0; k < 100; k++) {
                    supervisor.step(timeStep);
                }
                supervisor.getFromDef("edmo1").remove();
                supervisor.getFromDef("edmo2").remove();
            }
        }

        while (supervisor.step(timeStep) != -1) {
        }

    }

    private static void importEdmos(Supervisor supervisor, WebotsNode root) {
        root.importChildFromString(
            FileSystem
                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                .replaceAll("__id__", "1")
        );

        WebotsNode edmo1 = new WebotsNode(supervisor.getFromDef("edmo1"));
        edmo1.setTranslation(new Vector(2, 1, 2));

        root.importChildFromString(
            FileSystem
                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                .replaceAll("__id__", "2")
        );

        WebotsNode edmo2 = new WebotsNode(supervisor.getFromDef("edmo2"));
        edmo2.setTranslation(new Vector(0, 1, 0));
    }

    private static void connect(Supervisor supervisor, int timeStep, WebotsNode edmo1, WebotsNode edmo2, int connector1, int connector2) {
        WebotsNode connectorEdmo1 = new WebotsNode(SupervisorController.getConnectors(edmo1).get(connector1));
        WebotsNode connectorEdmo2 = new WebotsNode(SupervisorController.getConnectors(edmo2).get(connector2));

        Vector zAxisCE1 = connectorEdmo1.getZAxisOrientation();
        Vector zAxisCE2 = connectorEdmo2.getZAxisOrientation().multiply(-1);
        RotationVector vecRotFromCE1ToCE2 = zAxisCE1.rotationVector(zAxisCE2);
        System.out.println(zAxisCE1);
        System.out.println(zAxisCE2);
        System.out.println(vecRotFromCE1ToCE2);
        edmo1.setRotation(edmo1.getRotation().addRotation(vecRotFromCE1ToCE2));

        supervisor.step(timeStep);
        supervisor.simulationResetPhysics();

        Vector posCE1 = connectorEdmo1.getPosition();
        Vector posCE2 = connectorEdmo2.getPosition();
        Vector vecFromCE1ToCE2 = posCE2.subtract(posCE1);
        System.out.println(posCE1);
        System.out.println(posCE2);
        System.out.println(vecFromCE1ToCE2);
        edmo1.setTranslation(edmo1.getPosition().add(vecFromCE1ToCE2));

        supervisor.step(timeStep);
        supervisor.simulationResetPhysics();
    }

}
