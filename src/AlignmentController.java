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

        WebotsNode root = Supervisor.getRoot();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + " " + j);
                importEdmos();
                connect(
                    Supervisor.getFromDef("edmo1"),
                    Supervisor.getFromDef("edmo2"),
                    i, j
                );
                for (int k = 0; k < 100; k++) {
                    Supervisor.nextTimeStep();
                }
                Supervisor.getFromDef("edmo1").getNode().remove();
                Supervisor.getFromDef("edmo2").getNode().remove();
            }
        }

        while (Supervisor.nextTimeStep() != -1) {
        }

    }

    private static void importEdmos() {
        Supervisor.importEdmo("1", new Vector(2, 1, 2));
        Supervisor.importEdmo("2", new Vector(0, 1, 0));
    }

    private static void connect(WebotsNode addon, WebotsNode base, int connector1, int connector2) {
        WebotsNode connectorAddon = new WebotsNode(SupervisorController.getConnectors(addon).get(connector1));
        WebotsNode connectorBase = new WebotsNode(SupervisorController.getConnectors(base).get(connector2));

        addon.setRotation(new RotationVector(0, 1, 0, 0));

        Supervisor.update();

        Vector zAxisOfAddonConnector = connectorAddon.getZAxisOrientation();
        Vector zAxisOfBaseConnector = connectorBase.getZAxisOrientation().multiply(-1);
        RotationVector rotateFromAddonConnectorToBaseConnector =
            zAxisOfAddonConnector.rotationVector(zAxisOfBaseConnector);
        addon.setRotation(rotateFromAddonConnectorToBaseConnector);

        Supervisor.update();

        Vector addonConnectorPosition = connectorAddon.getPosition();
        Vector baseConnectorPosition = connectorBase.getPosition();
        Vector translationFromAddonConnectorToBaseConnector =
            baseConnectorPosition.subtract(addonConnectorPosition);
        addon.setTranslation(addon.getPosition().add(translationFromAddonConnectorToBaseConnector));

        Supervisor.update();
    }

}
