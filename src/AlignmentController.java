import Utility.FileSystem;
import Utility.RotationVector;
import Utility.Vector;
import Webots.Supervisor;
import Webots.WebotsNode;

import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        WebotsNode root = Webots.Supervisor.getRoot();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + " " + j);
                importEdmos();
                connect(
                    Webots.Supervisor.getFromDef("edmo1"),
                    Webots.Supervisor.getFromDef("edmo2"),
                    i, j
                );
                for (int k = 0; k < 100; k++) {
                    Supervisor.nextTimeStep();
                }
                Webots.Supervisor.getFromDef("edmo1").getNode().remove();
                Webots.Supervisor.getFromDef("edmo2").getNode().remove();
            }
        }

        while (Supervisor.nextTimeStep() != -1) {
        }

    }

    private static void importEdmos() {
        Webots.Supervisor.importEdmo("1", new Vector(2, 1, 2));
        Webots.Supervisor.importEdmo("2", new Vector(0, 1, 0));

    }

    private static void connect(WebotsNode edmo1, WebotsNode edmo2, int connector1, int connector2) {
        WebotsNode connectorEdmo1 = new WebotsNode(SupervisorController.getConnectors(edmo1).get(connector1));
        WebotsNode connectorEdmo2 = new WebotsNode(SupervisorController.getConnectors(edmo2).get(connector2));

        edmo1.setRotation(new RotationVector(0, 1, 0, 0));

        Webots.Supervisor.update();

        Vector zAxisCE1 = connectorEdmo1.getZAxisOrientation();
        Vector zAxisCE2 = connectorEdmo2.getZAxisOrientation().multiply(-1);
        RotationVector vecRotFromCE1ToCE2 = zAxisCE1.rotationVector(zAxisCE2);
        edmo1.setRotation(vecRotFromCE1ToCE2);

        Webots.Supervisor.update();

        Vector posCE1 = connectorEdmo1.getPosition();
        Vector posCE2 = connectorEdmo2.getPosition();
        Vector vecFromCE1ToCE2 = posCE2.subtract(posCE1);
        edmo1.setTranslation(edmo1.getPosition().add(vecFromCE1ToCE2));

        Webots.Supervisor.update();
    }

}
