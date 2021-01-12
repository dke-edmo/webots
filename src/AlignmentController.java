import EDMO.Connection;
import EDMO.Connection.Connector;
import EDMO.Connection.Orientation;
import EDMO.Module;
import EDMO.Structure;
import Utility.Matrix;
import Utility.RotationVector;
import Utility.Vector;
import Webots.*;
import com.cyberbotics.webots.controller.Field;
import com.cyberbotics.webots.controller.Node;

import java.util.*;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    static Map<Module, EDMO> modules = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        EDMO edmo1 = Supervisor.importEdmo("1", new Vector(0, 1, 0));
        EDMO edmo2 = Supervisor.importEdmo("2", new Vector(2, 1, 2));
        connect(
            edmo1, edmo2,
            edmo1.getConnector(0), edmo2.getConnector(1)
        );


        EDMO edmo3 = Supervisor.importEdmo("3", new Vector(0, 1, 0));
        EDMO edmo4 = Supervisor.importEdmo("4", new Vector(-2, 1, -2));
        connect(
            edmo3, edmo4,
            edmo3.getConnector(0), edmo4.getConnector(1)
        );

//        edmo1.getNode().remove();
//        edmo2.getNode().remove();

//        testAllConnections();

//        Module moduleA = new Module();
//        Module moduleL = new Module();
//        Module moduleR = new Module();
//
//        Structure structure = new Structure(
//            new Connection(moduleA, moduleL, Connector.L, Connector.B, Orientation.H),
//            new Connection(moduleA, moduleR, Connector.R, Connector.B, Orientation.H)
//        );
//
//        createStructure(structure);

        while (Supervisor.nextTimeStep() != -1) {
        }

    }

    private static void createConnection(Connection connection) {
        EDMO addon = getEDMO(connection.getModuleA());
        EDMO base = getEDMO(connection.getModuleB());
        connect(
            addon,
            base,
            addon.getConnector(connection.getConnectorA()),
            base.getConnector(connection.getConnectorB())
        );
    }

    private static void createStructure(Structure structure) {
        Set<Module> connectedModules = new HashSet<>();
        for(Connection connection: structure.getTopologicallySortedConnections()) {
            Module addonModule = connectedModules.contains(connection.getModuleA())
                ? connection.getModuleB() : connection.getModuleA();
            Module baseModule = connection.getOtherModule(addonModule);
            EDMO addon = getEDMO(addonModule);
            EDMO base = getEDMO(baseModule);
            connect(
                addon,
                base,
                addon.getConnector(connection.getConnectorByModule(addonModule)),
                base.getConnector(connection.getConnectorByModule(baseModule))
            );
            connectedModules.add(connection.getModuleA());
            connectedModules.add(connection.getModuleB());
        }
    }

    private static void testAllConnections() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + " " + j);
                EDMO edmo1 = Supervisor.importEdmo("1", new Vector(2, 1, 2));
                EDMO edmo2 = Supervisor.importEdmo("2", new Vector(0, 1, 0));
//                edmo2.setRotation(new RotationVector(Math.random(), Math.random(), Math.random(), Math.random()));
                connect(
                    edmo1, edmo2,
                    edmo1.getConnector(i), edmo2.getConnector(j)
                );
                for (int k = 0; k < 100; k++) {
                    Supervisor.nextTimeStep();
                }
                edmo1.getNode().remove();
                edmo2.getNode().remove();
            }
        }
    }

    private static EDMO getEDMO(Module module) {
        if(!modules.containsKey(module)) {
            int id = modules.size() + 1;
            modules.put(
                module,
                Supervisor.importEdmo(String.valueOf(id), new Vector(1, 1, 1).multiply(id))
            );
        }
        return modules.get(module);
    }

    private static void connect(WebotsNode addon, WebotsNode base, WebotsNode connectorAddon, WebotsNode connectorBase) {
        if(addon == base)
            throw new RuntimeException("One module can not be connected with itself!");
        if(connectorAddon == connectorBase)
            throw new RuntimeException("One connector can not be connected with itself!");

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
