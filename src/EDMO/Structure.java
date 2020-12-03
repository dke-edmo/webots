package EDMO;

import EDMO.Exceptions.InvalidStructure;

import java.util.*;

/**
 * Represents EDMO structure consisting of many modules connected together.
 *
 * @author Tomasz Darmetko
 */
public class Structure {

    private final Set<Module> modules = new HashSet<Module>();

    private final List<Connection> connections;
    private final Map<Module, Set<Connection>> connectionsPerModule = new HashMap<>();
    private final Map<Module, Map<Module, Connection>> connectionPerModules = new HashMap<>();

    public Structure(Connection ...connections) {
        this(Arrays.asList(connections));
    }

    public Structure(List<Connection> connections) {
        this.connections = connections;
        for (Connection connection: connections) {
            addModule(connection, connection.getModuleA());
            addModule(connection, connection.getModuleB());
        }
    }

    private void addModule(Connection connection, Module module) {

        Module otherModule = connection.getOtherModule(module);

        modules.add(module);

        if(!connectionPerModules.containsKey(module)) {
            Map<Module, Connection> moduleConnections = new HashMap<>();
            connectionPerModules.put(module, moduleConnections);
        }
        if(connectionPerModules.get(module).containsKey(otherModule)) {
            throw new InvalidStructure("Two modules can have only one connection!");
        }
        connectionPerModules.get(module).put(otherModule, connection);

        if(!connectionsPerModule.containsKey(module)) {
            Set<Connection> moduleConnections = new HashSet<>();
            connectionsPerModule.put(module, moduleConnections);
        }
        connectionsPerModule.get(module).add(connection);

    }

    public Set<Module> getModules() {
        return modules;
    }

    public List<Connection> getAllConnections() {
        return connections;
    }

    public Set<Connection> getConnections(Module module) {
        return connectionsPerModule.get(module);
    }

    public Connection getConnection(Module moduleA, Module moduleB) {
        return connectionPerModules.get(moduleA).get(moduleB);
    }

}
