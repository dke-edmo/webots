import Webots.IMUReadings;
import EDMO.*;
import EDMO.Module;


import Utility.*;
import Webots.IMUReading;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class SupervisorController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        WebotsNode root = new WebotsNode(supervisor.getRoot());

//        root.importChildFromString(
//            FileSystem
//                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
//                .replaceAll("__id__", "1")
//        );

        ObjectCommunicator<Double, IMUReading> communicator1 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter1"), supervisor.getReceiver("receiver1"), timeStep
        );

        ObjectCommunicator<Double, IMUReading> communicator2 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter2"), supervisor.getReceiver("receiver2"), timeStep
        );

        WebotsNode robot1 = new WebotsNode(supervisor.getFromDef("robot1"));
        WebotsNode robot2 = new WebotsNode(supervisor.getFromDef("robot2"));

        Node cRobot1 = getConnectors(robot1).get(2).getNode();
        Node cRobot2 = getConnectors(robot2).get(3).getNode();
        //translate(robot1, cRobot1, robot2, cRobot2);

        IMUReadings readings1 = new IMUReadings();
        IMUReadings readings2 = new IMUReadings();

        int counter = 0;
        while (supervisor.step(timeStep) != -1) {

            counter++;


            if(counter % 100 == 0) {
                double position = Math.PI / 2;
                communicator1.emit(position);
                communicator2.emit(position);
            }

            if(communicator1.hasNext()) {
                IMUReading reading = communicator1.receive();
                readings1.add(reading);
                System.out.println("Robot1: " + reading);
//                Vector rotationVector = reading.getLinear().rotationVector(yAxisVector);
//                System.out.println("Rotation Vector: " + rotationVector);
//                System.out.println("Expected vector: " + robot1.getRotation());
//                if(counter > 100 && counter % 20 == 0) robot2.setRotation(rotationVector);
            }

            if(communicator2.hasNext()) {
                IMUReading reading = communicator2.receive();
                readings2.add(reading);
                System.out.println("Robot2: " + reading);
            }

            if(counter == 1000) break;

        }

        Serializer.saveToFile(readings1, "../../resources/reading1.ser");
        Serializer.saveToFile(readings2, "../../resources/reading2.ser");

    }

    /*
        1. Add the first 3 connectors
        2. Search for Hinge Joint
        3. Search fot the last Connector in EndPoint
     */
    public static ArrayList<WebotsNode> getConnectors(WebotsNode robot) {
        ArrayList<WebotsNode> connectors = robot.getChildrenFromTypeName("Connector", "children");
        connectors = robot.getChildrenFromTypeName("Connector", "children");
        WebotsNode hingeJointNode = robot.getChildFromTypeName("HingeJoint", "children");
        WebotsNode childrenEndPoint = hingeJointNode.getChildNodeFromField("endPoint");
        connectors.add(childrenEndPoint.getChildFromTypeName("Connector", "children"));
        return connectors;
    }

    // Rotate around the z-axis to make to connectors facing each other
    public static void rotate(WebotsNode robot1, Node connectorR1, WebotsNode robot2, Node connectorR2){
        ArrayList<WebotsNode> connectors1 = getConnectors(robot1);
        ArrayList<WebotsNode> connectors2 = getConnectors(robot2);
        double[] rotationAroundZCounterClockwise = {0, 0, 1, Math.PI/2};
        Vector rotateCounter = new Vector(rotationAroundZCounterClockwise);
        // For robot 1 => Connector facing the right side
        boolean max = false;
        while(!max){
            int greaterX = 0;
            for(WebotsNode connector: connectors1){
                if(connector.getNode().getPosition()[0] > connectorR1.getPosition()[0]){
                    greaterX++;
                }
            }
            if(greaterX > 1) {
                robot1.setRotation(rotateCounter);
            }
            else{max=true;}

        }
        // Robot 2 => Connector facing the left side
        boolean min = false;
        while(!min){
            int smallerX = 0;
            for(WebotsNode connector: connectors2){
                if(connector.getNode().getPosition()[0] < connectorR2.getPosition()[0]){
                    smallerX++;
                }
            }
            if(smallerX > 1) {
                robot1.setRotation(rotateCounter);
            }
            else{min=true;}
        }
    }

    public static void translate(WebotsNode robot1, Node connectorR1, WebotsNode robot2, Node connectorR2){
        double[] robot1Center = robot1.getNode().getPosition();
        // Position of the center of the second robot compared to robot 1
        double goalXRobot2 = robot1Center[0] + connectorR1.getPosition()[0] - connectorR2.getPosition()[0];
        double goalYRobot2 = robot1Center[1] + connectorR2.getPosition()[1] - connectorR2.getPosition()[1];
        double goalZRobot2 = robot1Center[2] + connectorR2.getPosition()[2] - connectorR2.getPosition()[2];

        double[] finalRobot2Center = {goalXRobot2, goalYRobot2, goalZRobot2};
        robot2.getNode().getField("translation").setSFVec3f(finalRobot2Center);
    }


}
