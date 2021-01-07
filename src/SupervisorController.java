import Webots.IMUReadings;
import EDMO.*;
import EDMO.Module;


import Utility.*;
import Webots.IMUSensor.IMUReading;
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

        double[] rotate = {1, 1, 1, 0};
        robot1.setRotation(new Vector(rotate));
        robot2.setRotation(new Vector(rotate));

        Node cRobot1 = getConnectors(robot1).get(2);
        Node cRobot2 = getConnectors(robot2).get(3);
        //rotate(robot1, cRobot1, robot2, cRobot2);

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
    public static ArrayList<Node> getConnectors(WebotsNode robot){
        ArrayList<Node> connectors = robot.getChildrenFromTypeName("Connector", "children");
        Node hingeJointNode = robot.getChildFromTypeName("HingeJoint", "children");
        Field endPoint = hingeJointNode.getField("endPoint");
        Node childrenEndPoint = endPoint.getSFNode();
        connectors.add(new WebotsNode(childrenEndPoint).
                getChildFromTypeName("Connector", "children"));
        return connectors;
    }

    // Rotate around the z-axis to make to connectors facing each other
    public static void rotate(WebotsNode robot1, Node connectorR1, WebotsNode robot2, Node connectorR2){
        ArrayList<Node> connectors1 = getConnectors(robot1);
        ArrayList<Node> connectors2 = getConnectors(robot2);
        double[] rotationAroundZCounterClockwise = {0, 0, 1, Math.PI/2};
        Vector rotateCounter = new Vector(rotationAroundZCounterClockwise);

        // For robot 1 => Connector facing the right side
        boolean max = false;
        while(!max){
            int greaterX = 0;
            for(Node connector: connectors1){
                if(connector.getPosition()[0] > connectorR1.getPosition()[0]){
                    System.out.println("Connector " + connectorR1.getField("name") + " with x =" + connectorR1.getPosition()[0]
                            + " vs connector " + connector.getField("name") + " with x =" + connector.getPosition()[0]);
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
            for(Node connector: connectors2){
                if(connector.getPosition()[0] < connectorR2.getPosition()[0]){
                    System.out.println("Connector " + connectorR2.getField("name") + " with x =" + connectorR2.getPosition()[0]
                            + " vs connector " + connector.getField("name")+ " with x =" + connector.getPosition()[0]);
                    smallerX++;
                }
            }
            if(smallerX > 1) {
                robot1.setRotation(rotateCounter);
            }
            else{min=true;}
        }
    }

    public static void tranlsate(WebotsNode robot1, Node connectorR1, WebotsNode robot2, Node connectorR2){

    }



}
