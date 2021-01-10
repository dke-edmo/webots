package EDMO;

import EDMO.Exceptions.InvalidStructure;

/**
 * This class represents connection between two EDMO modules.
 *
 * Each module has 4 connectors - places where modules can be joined together.
 *
 * Connection can have also orientation either vertical or horizontal. We skip diagonal for now.
 *
 * @author Tomasz Darmetko
 */
public class Connection {

    public enum Connector {
        B, R, L, T, // Bottom, Right, Left, Top
    }

    public enum Orientation {
        H, V, // Horizontal, Vertical
    }

    private Module moduleA;
    private Module moduleB;

    private Connector connectorA;
    private Connector connectorB;

    private Orientation orientation;

    public Connection(
        Module moduleA,
        Module moduleB,
        Connector connectorA,
        Connector connectorB,
        Orientation orientation
    ) {
        if(moduleA == moduleB) {
            throw new InvalidStructure("A module can not have connection to itself: " + moduleA);
        }
        this.moduleA = moduleA;
        this.moduleB = moduleB;
        this.connectorA = connectorA;
        this.connectorB = connectorB;
        this.orientation = orientation;
    }

    public Module getModuleA() {
        return moduleA;
    }

    public Module getModuleB() {
        return moduleB;
    }

    public Module getOtherModule(Module module) {
        if(getModuleA() != module && getModuleB() != module) {
            throw new RuntimeException("Given modules is not part of connection: " + module);
        }
        return module == moduleA ? moduleB : moduleA;
    }

    public Connector getConnectorA() {
        return connectorA;
    }

    public Connector getConnectorB() {
        return connectorB;
    }

    public Connector getConnectorByModule(Module module) {
        if(module == moduleA) return connectorA;
        if(module == moduleB) return connectorB;
        throw new RuntimeException("Module: " + module + " does not belong to this connection!");
    }

    public Orientation getOrientation() {
        return orientation;
    }

}
