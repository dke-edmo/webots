package Webots;

import Utility.FileSystem;
import Utility.Vector;

public class Supervisor {

    private static final int timeStep;
    private static final com.cyberbotics.webots.controller.Supervisor supervisor;
    private static int edmoCounter = 1;

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

    public static void removeAllEdmos() {
        for (int i = 1; i < edmoCounter; i++) {
            getEdmoById(i).getNode().remove();
        }
        edmoCounter = 1;
    }

    public static EDMO importEdmo(Vector location) {
        String id = String.valueOf(edmoCounter++);
        getRoot()
            .importChildFromString(
                FileSystem
                    .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
                    .replaceAll("__id__", id)
            );

        EDMO edmo = getEdmoById(id);
        edmo.setTranslation(location);
        update();

        return edmo;
    }

    public static EDMO getEdmoById(int id) {
        return getEdmoById(String.valueOf(id));
    }

    public static EDMO getEdmoById(String id) {
        return new EDMO(
            supervisor.getFromDef("edmo" + id),
            new ObjectCommunicator<>(
                supervisor.getEmitter("emitter" + id),
                supervisor.getReceiver("receiver" + id),
                timeStep
            )
        );
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
