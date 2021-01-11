package Webots;

import com.cyberbotics.webots.controller.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EDMO extends WebotsNode {

    private ObjectCommunicator<Double, IMUReading> communicator;
    public EDMO(Node node, ObjectCommunicator<Double, IMUReading> communicator) {
        super(node);
        this.communicator = communicator;
    }

    public ObjectCommunicator<Double, IMUReading> getCommunicator() {
        return communicator;
    }

//    public static WebotsNode getConnector(WebotsNode edmo, int connectorId) {
//        return new WebotsNode(SupervisorController.getConnectors(edmo).get(connectorId));
//    }
//
//    public static WebotsNode getConnector(WebotsNode edmo, EDMO.Connection.Connector connector) {
//        return getConnectors(edmo).get(connector);
//    }
//
//    public static Map<EDMO.Connection.Connector, WebotsNode> getConnectors(WebotsNode edmo) {
//        ArrayList<Node> connectors = SupervisorC.getConnectors(edmo);
//        Map<EDMO.Connection.Connector, WebotsNode> connectorsMap = new HashMap<>();
//        connectorsMap.put(EDMO.Connection.Connector.B, new WebotsNode(connectors.get(0)));
//        connectorsMap.put(EDMO.Connection.Connector.L, new WebotsNode(connectors.get(1)));
//        connectorsMap.put(EDMO.Connection.Connector.R, new WebotsNode(connectors.get(2)));
//        connectorsMap.put(EDMO.Connection.Connector.T, new WebotsNode(connectors.get(3)));
//        return connectorsMap;
//    }

}
