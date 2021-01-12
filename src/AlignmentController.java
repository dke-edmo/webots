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
import java.util.stream.DoubleStream;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    static Map<Module, EDMO> modules = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        int connector1Hardware = 0;
        int connector2Hardware = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                runExperiment(connector1Hardware, connector2Hardware, i, j);
            }
        }

    }

    private static void runExperiment(int connector1Hardware, int connector2Hardware, int connector1Simulation, int connector2Simulation) {
        EDMOCollection hardware = createSimpleEDMOStructure(new Vector(0, 0, 0), connector1Hardware, connector2Hardware);
        EDMO edmo1 = hardware.getEdmos().get(0);
        EDMO edmo2 = hardware.getEdmos().get(1);

        EDMOCollection edmo1Collection = new EDMOCollection();
        edmo1Collection.add(edmo1);

        EDMOCollection edmo2Collection = new EDMOCollection();
        edmo2Collection.add(edmo2);

        EDMOCollection edmosSimulation = new EDMOCollection();
        for (int i = 1; i < 5; i++) {

            for (int m = -1; m <= 1; m+=2) {
                for (int n = -1; n <= 1; n+=2) {

                    EDMOCollection simulation
                        = createSimpleEDMOStructure(new Vector(m*3, 0, n*3).multiply(i), connector1Simulation, connector2Simulation);

                    edmo1Collection.add(simulation.getEdmos().get(0));
                    edmo2Collection.add(simulation.getEdmos().get(1));

                    edmosSimulation.add(simulation);

                }
            }

        }

        EDMOCollection allEdmos = new EDMOCollection();
        allEdmos.add(hardware);
        allEdmos.add(edmosSimulation);

        int counter = 0;
        IMUReadings readingsHardware = new IMUReadings();
        IMUReadings readingsSimulation = new IMUReadings();
        while (Supervisor.nextTimeStep() != -1) {
            if(counter < 100) {
                // find stable orientation
                if(counter == 10) allEdmos.getStream().forEach(e -> e.getCommunicator().emit(-Math.PI/2));
                if(counter == 70) allEdmos.getStream().forEach(e -> e.getCommunicator().emit(Math.PI/2));
            } else if(counter < 150) {
                // stop movement
                allEdmos.clearReceiver();
                allEdmos.emit(0);
            } else if(counter < 250) {
                if(counter == 150) edmo1Collection.emit(Math.PI/2);
                if(counter == 200) edmo1Collection.emit(-Math.PI/2);
                if(counter == 150) edmo2Collection.emit(Math.PI/2);
                if(counter == 200) edmo2Collection.emit(-Math.PI/2);
            } else if(counter == 250) {

                readingsHardware = edmo1.getIMUReadings();
                Vector readingsVecHardware = readingsHardware.toVector();

                Map<String, List<Double>> metrics = new HashMap<>();
                metrics.put("Normalized Vector Similarity", new ArrayList<>());
                metrics.put("Not Normalized Vector Similarity", new ArrayList<>());
                metrics.put("Normalized Vector Distance", new ArrayList<>());
                metrics.put("Not Normalized Vector Distance", new ArrayList<>());

                for (EDMO edmo: edmo1Collection.getEdmos()) {

                    readingsSimulation = edmo.getIMUReadings();
                    Vector readingsVecSimulation = readingsSimulation.toVector();

                    metrics.get("Normalized Vector Similarity")
                        .add(readingsVecHardware.normalize().cosineSimilarity(readingsVecSimulation.normalize()));
                    metrics.get("Not Normalized Vector Similarity")
                        .add(readingsVecHardware.normalize().cosineSimilarity(readingsVecSimulation.normalize()));

                    metrics.get("Normalized Vector Distance")
                        .add(readingsVecHardware.euclideanDistance(readingsVecSimulation));
                    metrics.get("Not Normalized Vector Distance")
                        .add(readingsVecHardware.euclideanDistance(readingsVecSimulation));
                }

                System.out.println(
                    "Hardware: " + connector1Hardware + " " + connector2Hardware + " " +
                    "Simulation: " + connector1Simulation + " " + connector2Simulation
                );

                for (Map.Entry<String, List<Double>> entry: metrics.entrySet()) {
                    System.out.println(
                        entry.getKey() + ": " +
                        entry.getValue().stream().mapToDouble(v -> v).average().getAsDouble()
                    );
                }

           } else {
                Supervisor.removeAllEdmos();
                break;
            }
            counter++;
        }
    }

    private static EDMOCollection createSimpleEDMOStructure(Vector placement, int connectorId1, int connectorId2) {
        EDMO edmo1 = Supervisor.importEdmo(new Vector(0, 1, 0).add(placement));
        EDMO edmo2 = Supervisor.importEdmo(new Vector(1, 1, 1).add(placement));
        connect(
            edmo1, edmo2,
            edmo1.getConnector(connectorId1), edmo2.getConnector(connectorId2)
        );
        return new EDMOCollection(edmo1, edmo2);
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
                EDMO edmo1 = Supervisor.importEdmo(new Vector(2, 1, 2));
                EDMO edmo2 = Supervisor.importEdmo(new Vector(0, 1, 0));
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
            modules.put(module, Supervisor.importEdmo(new Vector(1, 1, 1).multiply(id)));
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
