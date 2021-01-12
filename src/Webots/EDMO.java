package Webots;

import com.cyberbotics.webots.controller.Field;
import com.cyberbotics.webots.controller.Node;
import EDMO.Connection.Connector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDMO extends WebotsNode {

    private final ObjectCommunicator<Double, IMUReading> communicator;
    private List<WebotsNode> connectorsList = new ArrayList<>();
    private final Map<Connector, WebotsNode> connectorsMap = new HashMap<>();

    public EDMO(Node node, ObjectCommunicator<Double, IMUReading> communicator) {
        super(node);
        this.communicator = communicator;
        retrieveConnectors();
    }

    private void retrieveConnectors() {

        connectorsList = getChildrenFromTypeName("Connector", "children");
        WebotsNode hingeJointNode = getChildFromTypeName("HingeJoint", "children");
        WebotsNode childrenEndPoint = hingeJointNode.getChildNodeFromField("endPoint");
        connectorsList.add(childrenEndPoint.getChildFromTypeName("Connector", "children"));

        connectorsMap.put(Connector.B, connectorsList.get(0));
        connectorsMap.put(Connector.L, connectorsList.get(1));
        connectorsMap.put(Connector.R, connectorsList.get(2));
        connectorsMap.put(Connector.T, connectorsList.get(3));

    }

    public ObjectCommunicator<Double, IMUReading> getCommunicator() {
        return communicator;
    }

    public WebotsNode getConnector(int connectorId) {
        return connectorsList.get(connectorId);
    }

    public WebotsNode getConnector(Connector connector) {
        return connectorsMap.get(connector);
    }

}
