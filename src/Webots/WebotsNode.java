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

    /**
     * @see Vector#rotationVector
     * @link https://cyberbotics.com/doc/reference/transform#field-summary
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_node_get_field
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_field_get_sf_rotation
     */
    public Vector getRotation() {
        return new Vector(node.getField("rotation").getSFRotation());
    }

    /**
     * @see Vector#rotationVector
     * @link https://cyberbotics.com/doc/reference/transform#field-summary
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_node_get_field
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_field_set_sf_rotation
     */
    public void setRotation(Vector rotationVector) {
        node.getField("rotation").setSFRotation(rotationVector.getDoubleArray());
    }

}
