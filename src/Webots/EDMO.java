package Webots;

import com.cyberbotics.webots.controller.Node;

public class EDMO extends WebotsNode {

    private ObjectCommunicator<Double, IMUSensor.IMUReading> communicator;
    public EDMO(Node node, ObjectCommunicator<Double, IMUSensor.IMUReading> communicator) {
        super(node);
        this.communicator = communicator;
    }

    public ObjectCommunicator<Double, IMUSensor.IMUReading> getCommunicator() {
        return communicator;
    }

}
