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

    private final Map<Module, Set<Module>> modulesConnectedToModule = new HashMap<>();
    private final Map<Module, Map<Connection.Connector, Module>> moduleByConnectorToModule = new HashMap<>();

    public Structure(Connection ...connections) {
        this(Arrays.asList(connections));
    }

    public Structure(List<Connection> connections) {
        this.connections = connections;
        for (Connection connection: connections) {
            addModule(connection, connection.getModuleA());
            addModule(connection, connection.getModuleB());
        }
        validate();
    }

    private void addModule(Connection connection, Module module) {

        Module otherModule = connection.getOtherModule(module);
        Connection.Connector connector = connection.getConnectorByModule(module);

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

        if(!modulesConnectedToModule.containsKey(module)) {
            Set<Module> modulesConnected = new HashSet<>();
            modulesConnectedToModule.put(module, modulesConnected);
        }
        modulesConnectedToModule.get(module).add(otherModule);
        if(modulesConnectedToModule.get(module).size() > 4) {
            throw new RuntimeException("Module " + module + " is connected to more than 4 other modules!");
        }

        if(!moduleByConnectorToModule.containsKey(module)) {
            Map<Connection.Connector, Module> moduleByConnector = new HashMap<>();
            moduleByConnectorToModule.put(module, moduleByConnector);
        }
        if(moduleByConnectorToModule.get(module).containsKey(connector)) {
            throw new InvalidStructure("One connector can connect only two modules!");
        }
        moduleByConnectorToModule.get(module).put(connector, otherModule);

    }

    private void validate() {
        Module current = modules.iterator().next();
        validateNoCycles(current, null, new HashSet<>());
        validateFullyConnected(current, new HashSet<>());
    }

    private void validateNoCycles(Module current, Module previous, Set<Module> visited) {
        if(visited.size() == modules.size() && !visited.contains(current)) return;
        if(visited.contains(current)) throw new RuntimeException("Structure contains cycle! " + visited + " " + current);
        visited.add(current);
        for (Module module: getConnectedModules(current)) {
            if(module == previous) continue; // simple cycles are allowed
            validateNoCycles(module, current, visited);
        }
        visited.remove(current);
    }

    private boolean validateFullyConnected(Module current, Set<Module> visited) {
        visited.add(current);
        if(visited.size() == modules.size()) return true;
        for (Module module: getConnectedModules(current)) {
            if(visited.contains(module)) continue;
            if(validateFullyConnected(module, visited)) return true;
        }
        throw new RuntimeException("Not all modules are connected together!");
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

    public Set<Module> getConnectedModules(Module module) {
        return modulesConnectedToModule.get(module);
    }

    public Connection getConnection(Module moduleA, Module moduleB) {
        return connectionPerModules.get(moduleA).get(moduleB);
    }

}
