package Webots;

import Utility.FileSystem;
import Utility.Vector;

import java.util.Map;

public class Supervisor {

    private static final int timeStep;
    private static final com.cyberbotics.webots.controller.Supervisor supervisor;

    static {
        supervisor = new com.cyberbotics.webots.controller.Supervisor();
        timeStep = (int)Math.round(supervisor.getBasicTimeStep());
    }

    public static int getTimeStep() {
        return timeStep;
    }

    public static com.cyberbotics.webots.controller.Supervisor getSupervisor() {
        return supervisor;
    }

    public static EDMO importEdmo(String id, Vector location) {

        getRoot()
            .importChildFromString(
                FileSystem
                    .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                    .replaceAll("__id__", id)
            );

        ObjectCommunicator<Double, IMUSensor.IMUReading> communicator = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter" + id), supervisor.getReceiver("receiver" + id), timeStep
        );

        EDMO edmo = new EDMO(
            supervisor.getFromDef("edmo" + id),
            communicator
        );
        edmo.setTranslation(location);
        update();

        return edmo;

    }

    public static WebotsNode getFromDef(String def) {
        return new WebotsNode(supervisor.getFromDef(def));
    }

    public static WebotsNode getRoot() {
        return new WebotsNode(supervisor.getRoot());
    }

    public static int nextTimeStep() {
        return supervisor.step(timeStep);
    }

    public static void update() {
        supervisor.simulationResetPhysics();
        nextTimeStep();
        supervisor.simulationResetPhysics();
    }

}
