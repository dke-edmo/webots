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
        getTopologicallySortedModules(current, new ArrayList<>(), true);
        getTopologicallySortedConnections(connections.iterator().next(), new ArrayList<>(), true);
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

    private List<Module> getTopologicallySortedModules(Module current, List<Module> visited, boolean root) {
        visited.add(current);
        if(visited.size() == modules.size()) return visited;
        for (Module module: getConnectedModules(current)) {
            if(visited.contains(module)) continue;
            visited = getTopologicallySortedModules(module, visited, false);
            if(visited.size() == modules.size()) return visited;
        }
        if(!root) return visited;
        List<Module> missingModules = new ArrayList<Module>(modules);
        missingModules.removeAll(visited);
        throw new RuntimeException("Not all modules are connected together! " + missingModules);
    }

    private List<Connection> getTopologicallySortedConnections(Connection current, List<Connection> visited, boolean root) {
        visited.add(current);
        if(visited.size() == connections.size()) return visited;
        for (Connection connection: getConnections(current.getModuleA())) {
            if(visited.contains(connection)) continue;
            visited = getTopologicallySortedConnections(connection, visited, false);
            if(visited.size() == connections.size()) return visited;
        }
        for (Connection connection: getConnections(current.getModuleB())) {
            if(visited.contains(connection)) continue;
            visited = getTopologicallySortedConnections(connection, visited, false);
            if(visited.size() == connections.size()) return visited;
        }
        if(!root) return visited;
        List<Connection> missingConnections = new ArrayList<Connection>(connections);
        missingConnections.removeAll(visited);
        throw new RuntimeException("Not all modules are connected together! " + missingConnections);
    }

    public Set<Module> getModules() {
        return modules;
    }

    public List<Module> getTopologicallySortedModules() {
        return getTopologicallySortedModules(modules.iterator().next(), new ArrayList<>(), true);
    }

    public List<Connection> getTopologicallySortedConnections() {
        return getTopologicallySortedConnections(connections.iterator().next(), new ArrayList<>(), true);
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
