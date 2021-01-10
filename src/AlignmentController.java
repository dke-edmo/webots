import EDMO.Connection;
import EDMO.Connection.Connector;
import EDMO.Connection.Orientation;
import EDMO.Module;
import EDMO.Structure;
import Utility.RotationVector;
import Utility.Vector;
import Webots.Supervisor;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.Field;
import com.cyberbotics.webots.controller.Node;

import java.util.*;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        testAllConnections();

//        Module moduleA = new Module();
//        Module moduleL = new Module();
//        Module moduleR = new Module();
//
//        Structure structure = new Structure(
//            new Connection(moduleA, moduleL, Connector.L, Connector.B, Orientation.H),
//            new Connection(moduleA, moduleR, Connector.R, Connector.B, Orientation.H)
//        );
//
//        Map<Module, WebotsNode> modules = new HashMap<>();
//        int counter = 0;
//        for (Module module: structure.getModules()) {
//            modules.put(
//                module,
//                Supervisor.importEdmo(String.valueOf(counter), new Vector(1, 1, 1).multiply(counter))
//            );
//        }
//
//        Set<Module> connectedModules = new HashSet<>();
//        for(Connection connection: structure.getTopologicallySortedConnections()) {
//            Module addonModule = connectedModules.contains(connection.getModuleA())
//                ? connection.getModuleB() : connection.getModuleA();
//            WebotsNode addon = modules.get(addonModule);
//            WebotsNode base = modules.get(connection.getOtherModule(addonModule));
//            connect(
//                addon,
//                base,
//                getConnector(addon, connection.getConnectorA()),
//                getConnector(base, connection.getConnectorB())
//            );
//            connectedModules.add(connection.getModuleA());
//            connectedModules.add(connection.getModuleB());
//        }

        while (Supervisor.nextTimeStep() != -1) {
        }

    }

    private static void testAllConnections() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + " " + j);
                importEdmos();
                WebotsNode edmo1 = Supervisor.getFromDef("edmo1");
                WebotsNode edmo2 = Supervisor.getFromDef("edmo2");
                connect(
                    edmo1, edmo2,
                    getConnector(edmo1, i), getConnector(edmo2, j)
                );
                for (int k = 0; k < 100; k++) {
                    Supervisor.nextTimeStep();
                }
                edmo1.getNode().remove();
                edmo2.getNode().remove();
            }
        }
    }

    private static void importEdmos() {
        Supervisor.importEdmo("1", new Vector(2, 1, 2));
        Supervisor.importEdmo("2", new Vector(0, 1, 0));
    }

    private static void connect(WebotsNode addon, WebotsNode base, WebotsNode connectorAddon, WebotsNode connectorBase) {

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

    public static WebotsNode getConnector(WebotsNode edmo, int connectorId) {
        return new WebotsNode(SupervisorController.getConnectors(edmo).get(connectorId));
    }

    public static WebotsNode getConnector(WebotsNode edmo, Connector connector) {
        return getConnectors(edmo).get(connector);
    }

    public static Map<Connector, WebotsNode> getConnectors(WebotsNode edmo) {
        ArrayList<Node> connectors = SupervisorController.getConnectors(edmo);
        Map<Connector, WebotsNode> connectorsMap = new HashMap<>();
        connectorsMap.put(Connector.B, new WebotsNode(connectors.get(0)));
        connectorsMap.put(Connector.L, new WebotsNode(connectors.get(1)));
        connectorsMap.put(Connector.R, new WebotsNode(connectors.get(2)));
        connectorsMap.put(Connector.T, new WebotsNode(connectors.get(3)));
        return connectorsMap;
    }

}
