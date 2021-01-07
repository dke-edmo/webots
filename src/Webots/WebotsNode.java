package Webots;

import EDMO.Module;
import Utility.Vector;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Field;

import java.util.ArrayList;

public class WebotsNode extends Module {

    private final Node node;

    public WebotsNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void importChildFromString(String definition) {
        node
            .getField("children")
            .importMFNodeFromString(
                -1, definition
            );
    }

    public Node getChildFromTypeName(String typeName, String subTree){
        int count = 0;
        Field children = node.getField(subTree);
        Node childNode;
        if ((childNode = children.getSFNode()) != null && childNode.getTypeName().equals(typeName)){
            return childNode;
        }
        while((childNode = children.getMFNode(count)) != null){
            if(childNode.getTypeName().equals(typeName)){
                return childNode;
            }
            count++;
        }
        return null;
    }

    public ArrayList<Node> getChildrenFromTypeName(String typeName, String subTree){
        int count = 0;
        ArrayList<Node> childrenNodes = new ArrayList<>();
        Field children = node.getField(subTree);
        Node childNode;
        while((childNode = children.getMFNode(count)) != null){
            if(childNode.getTypeName().equals(typeName)){
                childrenNodes.add(childNode);
            }
            count++;
        }
        return childrenNodes;
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

    public Vector getPosition() {
        return new Vector(node.getPosition());
    }

    public void setPosition(Vector positionVector) {
        node.getField("translation").setSFVec3f(positionVector.getDoubleArray());
    }

}
