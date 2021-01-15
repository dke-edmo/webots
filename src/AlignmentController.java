import EDMO.Connection;
import EDMO.Module;
import EDMO.Structure;
import Utility.Matrix;
import Utility.Metrics.*;
import Utility.RotationVector;
import Utility.Vector;
import Webots.*;

import java.util.*;

/**
 * @author Tomasz Darmetko
 */
public class AlignmentController {

    static Map<Module, EDMO> modules = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        testAllConnections();

        Module moduleT = new Module();
        Module moduleTL = new Module();
        Module moduleTR = new Module();
        Module moduleB = new Module();
        Module moduleBL = new Module();
        Module moduleBR = new Module();
        moduleT.name = "T";
        moduleTL.name = "TL";
        moduleTR.name = "TR";
        moduleB.name = "B";
        moduleBL.name = "BL";
        moduleBR.name = "BR";

        createStructure(new Structure(
            new Connection(moduleT, moduleB, Connection.Connector.B, Connection.Connector.T, Connection.Orientation.H),
            new Connection(moduleT, moduleTL, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
            new Connection(moduleT, moduleTR, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H),
            new Connection(moduleB, moduleBL, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
            new Connection(moduleB, moduleBR, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H)
        ));

//        int connector1Hardware = 0;
//        int connector2Hardware = 1;
//
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                runExperiment(connector1Hardware, connector2Hardware, i, j);
//            }
//        }

    }

    private static void runExperiment(int connector1Hardware, int connector2Hardware, int connector1Simulation, int connector2Simulation) {
        EDMOCollection hardware = createSimpleEDMOStructure(new Vector(0, 0, 0), connector1Hardware, connector2Hardware);
        EDMO edmo1H = hardware.getEdmos().get(0);
        EDMO edmo2H = hardware.getEdmos().get(1);

        EDMOCollection edmo1Collection = new EDMOCollection();
        edmo1Collection.add(edmo1H);

        EDMOCollection edmo2Collection = new EDMOCollection();
        edmo2Collection.add(edmo2H);

        Map<EDMO, EDMO> edmo1To2 = new HashMap<>();
        edmo1To2.put(edmo1H, edmo2H);

        EDMOCollection edmosSimulation = new EDMOCollection();
        for (int i = 1; i < 2; i++) {

            for (int m = -1; m <= 1; m+=2) {
                for (int n = -1; n <= 1; n+=2) {

                    EDMOCollection simulation
                        = createSimpleEDMOStructure(new Vector(m*4, 0, n*4).multiply(i), connector1Simulation, connector2Simulation);

                    edmo1To2.put(simulation.getEdmos().get(0), simulation.getEdmos().get(1));

                    edmo1Collection.add(simulation.getEdmos().get(0));
                    edmo2Collection.add(simulation.getEdmos().get(1));

                    edmosSimulation.add(simulation);

                }
            }

        }

        EDMOCollection allEdmos = new EDMOCollection();
        allEdmos.add(hardware);
        allEdmos.add(edmosSimulation);

        Metrics relationMetrics = new Metrics();
        Metrics allReadingsMetrics = new Metrics();
        Metrics linearReadingsMetrics = new Metrics();
        Metrics angularReadingsMetrics = new Metrics();

        int counter = 0;
        while (Supervisor.nextTimeStep() != -1) {
            if(counter < 100) {
                // find stable orientation
                if(counter == 10) allEdmos.emit(-Math.PI/2);
                if(counter == 70) allEdmos.emit(Math.PI/2);
            } else if(counter < 150) {
                // stop movement
                allEdmos.clearReceiver();
                allEdmos.emit(0);
            } else if(counter < 400) {
                if(counter == 150) edmo1Collection.emit(Math.PI/2);
                if(counter == 200) edmo1Collection.emit(-Math.PI/2);
                if(counter == 250) edmo2Collection.emit(Math.PI/2);
                if(counter == 300) edmo2Collection.emit(-Math.PI/2);
            } else if(counter == 400) {

                IMUReadings readingsEDMO1H = edmo1H.getIMUReadings();
                Vector allReadingsEDMO1H = readingsEDMO1H.toVector();
                Vector linearReadingsEDMO1H = readingsEDMO1H.getLinearAccelerationReadings();
                Vector angularReadingsEDMO1H = readingsEDMO1H.getAngularAccelerationReadings();

                Vector relationH = edmo1H.getLastLinearAccelerationReading()
                    .crossProduct(edmo2H.getLastLinearAccelerationReading());

                for (EDMO edmo1S: edmo1Collection.getEdmos()) {

                    EDMO edmo2S = edmo1To2.get(edmo1S);

//                    double similarityOfOrientation = edmo1H.getLastLinearAccelerationReading()
//                        .cosineSimilarity(edmo1S.getLastLinearAccelerationReading());

                    Vector relationS = edmo1S.getLastLinearAccelerationReading()
                        .crossProduct(edmo2S.getLastLinearAccelerationReading());

                    IMUReadings readingsEDMO1S = edmo1S.getIMUReadings();
                    Vector allReadingsEDMO1S = readingsEDMO1S.toVector();
                    Vector linearReadingsEDMO1S = readingsEDMO1S.getLinearAccelerationReadings();
                    Vector angularReadingsEDMO1S = readingsEDMO1S.getAngularAccelerationReadings();

                    relationMetrics.addResult(relationH, relationS);
                    allReadingsMetrics.addResult(allReadingsEDMO1H, allReadingsEDMO1S);
                    linearReadingsMetrics.addResult(linearReadingsEDMO1H, linearReadingsEDMO1S);
                    angularReadingsMetrics.addResult(angularReadingsEDMO1H, angularReadingsEDMO1S);

                }

                System.out.print(
                    "H" + connector1Hardware + connector2Hardware + " " +
                    "S" + connector1Simulation + connector2Simulation
                );
                System.out.print(",    ");

                relationMetrics.print();
                System.out.print("    ");
                allReadingsMetrics.print();
                System.out.print("    ");
                linearReadingsMetrics.print();
                System.out.print("    ");
                angularReadingsMetrics.print();

                System.out.println();

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
                for (int k = 0; k < 4; k++) {
                    if(j == k) continue;
                    for (int l = 0; l < 4; l++) {
                        System.out.println(i + " " + j);
                        EDMO edmo1 = Supervisor.importEdmo(new Vector(4, 1, 4));
                        EDMO edmo2 = Supervisor.importEdmo(new Vector(2, 1, 2));
    //                edmo2.setRotation(new RotationVector(Math.random(), Math.random(), Math.random(), Math.random()));
                        connect(
                            edmo1, edmo2,
                            edmo1.getConnector(i), edmo2.getConnector(j)
                        );
                        EDMO edmo3 = Supervisor.importEdmo(new Vector(0, 1, 0));
                        connect(
                            edmo3, edmo2,
                            edmo3.getConnector(l), edmo2.getConnector(k)
                        );
                        for (int m = 0; m < 300; m++) {
                            Supervisor.nextTimeStep();
                        }
                        Supervisor.removeAllEdmos();
                    }
                }
            }
        }
    }

    private static EDMO getEDMO(Module module) {
        if(!modules.containsKey(module)) {
            int id = modules.size() + 1;
            modules.put(module, Supervisor.importEdmo(new Vector(3, 0, 3).multiply(id).add(5)));
        }
        return modules.get(module);
    }

    private static void connect(EDMO addon, EDMO base, WebotsNode connectorAddon, WebotsNode connectorBase) {
        if(addon == base)
            throw new RuntimeException("One module can not be connected with itself!");
        if(connectorAddon == connectorBase)
            throw new RuntimeException("One connector can not be connected with itself!");

        System.out.println("A:" + addon + " CA:" + connectorAddon + " B:" + base + " CB:" + connectorBase);

        addon.setRotation(new RotationVector(0, 1, 0, 0));

        Supervisor.update();

        Matrix rotate180degreesAroundX = new Matrix(new double[][]{{1,0,0}, {0,-1,0}, {0,0,-1}});
        Matrix rotationAddonConnector = connectorAddon.getRotationMatrix();
        Matrix rotationBaseConnector = connectorBase.getRotationMatrix().multiply(rotate180degreesAroundX);

        Vector zAxisOfAddonConnector = connectorAddon.getZAxisOrientation();
        Vector zAxisOfBaseConnector = connectorBase.getZAxisOrientation().multiply(-1);
        RotationVector rotateFromAddonConnectorToBaseConnector =
            zAxisOfAddonConnector.rotationVector(zAxisOfBaseConnector);

        Matrix rotationAddonConnectorInverse = rotationAddonConnector.inverse();

        System.out.println("-------------------------------------");
        addon.setRotation(rotationBaseConnector.multiply(rotationAddonConnectorInverse).getRotationVector());

        Supervisor.update();

        Vector addonConnectorPosition = connectorAddon.getPosition();
        Vector baseConnectorPosition = connectorBase.getPosition();
        Vector translationFromAddonConnectorToBaseConnector =
            baseConnectorPosition.subtract(addonConnectorPosition);

        connectorAddon.getNode().getField("isLocked").setSFBool(false);
        connectorBase.getNode().getField("isLocked").setSFBool(false);

        addon.setTranslation(addon.getPosition().add(translationFromAddonConnectorToBaseConnector));

        Supervisor.update();

        connectorAddon.getNode().getField("isLocked").setSFBool(true);
        connectorBase.getNode().getField("isLocked").setSFBool(true);

        Supervisor.update();

    }
}
