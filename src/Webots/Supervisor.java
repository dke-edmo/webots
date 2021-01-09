package Webots;

import Utility.FileSystem;

import java.util.Map;

public class Supervisor {

    private static com.cyberbotics.webots.controller.Supervisor supervisor = null;

    public static void setSupervisor(com.cyberbotics.webots.controller.Supervisor theSupervisor) {
        assert supervisor == null;
        supervisor = theSupervisor;
    }

    public static com.cyberbotics.webots.controller.Supervisor getSupervisor() {
        return supervisor;
    }

    public static void update() {
        getSupervisor()
            .getRoot()
            .getField("children")
            .importMFNodeFromString(
                -1, "DEF __hack_reset Solid {}"
            );

        getSupervisor()
            .getFromDef("__hack_reset")
            .remove();

        getSupervisor().simulationResetPhysics();

    }
}
