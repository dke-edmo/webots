package Webots;

import Utility.Vector;
import com.cyberbotics.webots.controller.Node;

public class WebotsNode {

    private final Node node;

    public WebotsNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Vector getRotation() {
        return new Vector(node.getField("rotation").getSFRotation());
    }

    public void setRotation(Vector rotationVector) {
        node.getField("rotation").setSFRotation(rotationVector.getDoubleArray());
    }

}
