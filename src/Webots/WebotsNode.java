package Webots;

import EDMO.Module;
import Utility.Matrix;
import Utility.RotationVector;
import Utility.Vector;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Field;

import java.util.ArrayList;

public class WebotsNode extends Module {

    private final Node node;

    public WebotsNode(Node node) {
        this.node = node;
    }

    public Field getPhysics() {
        return node.getField("physics");
    }

    public String getDef() {
        return node.getDef();
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

    public WebotsNode getChildNodeFromField(String field) {
        return new WebotsNode(node.getField(field).getSFNode());
    }

    public WebotsNode getChildFromTypeName(String typeName, String subTree) {
        Field childrenFiled = node.getField(subTree);
        for (int i = 0; i < childrenFiled.getCount(); i++) {
            Node childNode = childrenFiled.getMFNode(i);
            if(childNode.getTypeName().equals(typeName)) {
                return new WebotsNode(childNode);
            }
        }
        throw new RuntimeException("Node of type: " + typeName + " not found in sub tree " + subTree);
    }

    public ArrayList<WebotsNode> getChildrenFromTypeName(String typeName, String subTree) {
        Field childrenFiled = node.getField(subTree);
        ArrayList<WebotsNode> childrenNodes = new ArrayList<>();
        for (int i = 0; i < childrenFiled.getCount(); i++) {
            Node childNode = childrenFiled.getMFNode(i);
            if(childNode.getTypeName().equals(typeName)){
                childrenNodes.add(new WebotsNode(childNode));
            }
        }
        return childrenNodes;
    }

    /**
     * This function returns a matrix containing exactly 9 values
     * that shall be interpreted as a 3 x 3 orthogonal rotation matrix:
     * [ R[0] R[1] R[2] ]
     * [ R[3] R[4] R[5] ]
     * [ R[6] R[7] R[8] ]
     * Each column of the matrix represents where each of
     * the three main axes (x, y and z) is pointing in the node's coordinate system.
     *
     * @link https://cyberbotics.com/doc/reference/supervisor#wb_supervisor_node_get_orientation
     */
    public Vector getOrientation() {
        return new Vector(node.getOrientation());
    }

    public Vector getXAxisOrientation() {
        return getOrientation().getEveryNthValue(3, 0);
    }

    public Vector getYAxisOrientation() {
        return getOrientation().getEveryNthValue(3, 1);
    }

    public Vector getZAxisOrientation() {
        return getOrientation().getEveryNthValue(3, 2);
    }

    public Matrix getRotationMatrix() {
        return getOrientation().asMatrix(3, 3);
    }

    /**
     * @see Vector#rotationVector
     * @link https://cyberbotics.com/doc/reference/transform#field-summary
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_node_get_field
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_field_get_sf_rotation
     */
    public RotationVector getRotation() {
        return new RotationVector(node.getField("rotation").getSFRotation());
    }

    /**
     * @see Vector#rotationVector
     * @link https://cyberbotics.com/doc/reference/transform#field-summary
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_node_get_field
     * @link https://cyberbotics.com/doc/reference/supervisor?tab-language=java#wb_supervisor_field_set_sf_rotation
     */
    public void setRotation(Vector rotationVector) {
        node.getField("rotation").setSFRotation(rotationVector.getDoubleArray());
//        Webots.Supervisor.update();
//        node.resetPhysics();
    }

    public Vector getPosition() {
        return new Vector(node.getPosition());
    }

    public Vector getTranslation() {
        return new Vector(node.getField("translation").getSFVec3f());
    }

    public void setTranslation(Vector positionVector) {
        node.getField("translation").setSFVec3f(positionVector.getDoubleArray());
//        Webots.Supervisor.update();
//        node.resetPhysics();
    }

    public String toString() {
        return "Node{" + getDef() + "}";
    }

}
