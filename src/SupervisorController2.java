import Graph.Graph;
import Graph.ObjectFactory;
import Locomotion.CPGNeural;
import Locomotion.Leg;
import Locomotion.Opt;
import Webots.IMUReadings;
import Utility.*;
import Webots.IMUSensor.IMUReading;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.*;

import java.time.Clock;
import java.util.Arrays;
public class SupervisorController2 {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("bit");
        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        WebotsNode root = new WebotsNode(supervisor.getRoot());

//        root.importChildFromString(
//            FileSystem
//                .readString(FileSystem.webotsDirectory + "/EDMO.wbo")
//                .replaceAll("__id__", "1")
//        );
        int num_robots= 0;

        boolean more = true;

        while (more) {
            num_robots++;
            if(supervisor.getFromDef("robot" + num_robots) == null){
                num_robots--;
                System.out.println("THERE robots: "+num_robots);
                more = false;
            }
        }

        ObjectCommunicator<Double, IMUReading>[] communicators = new ObjectCommunicator[num_robots];
        WebotsNode[] robots = new WebotsNode[num_robots];
        IMUReadings[] readings = new IMUReadings[num_robots];

        for(int i = 1; i<=num_robots; i++){
            communicators[i-1] = new ObjectCommunicator<>(
                    supervisor.getEmitter("emitter"+i), supervisor.getReceiver("receiver"+i), timeStep
            );
            robots[i-1] = new WebotsNode(supervisor.getFromDef("robot"+i));
            readings[i-1] = new IMUReadings();
        }


        Vector yAxisVector = new Vector(0, 1, 0);

        //ObjectFactory graph = new ObjectFactory("robot7");


        long previousTime = Clock.systemDefaultZone().millis();

        CPGNeural cpgNeural1 = new CPGNeural();
        CPGNeural cpgNeural3 = new CPGNeural();
        CPGNeural cpgNeural5 = new CPGNeural();
        CPGNeural cpgNeural7 = new CPGNeural();


        //Create an Object Factory which will produce the graph for the robot, and possibly later objects
        //Specify which robot you want to use
        ObjectFactory factory = new ObjectFactory("robot7"); //make own robot
        //Retrieve the robot
        Graph graph = factory.getRobot();
        //Display the nodes belonging to that object, which is the robot
        graph.printNodes();
        graph.think();


        int counter = 0;
        int leg_counter = 0;
        while (supervisor.step(timeStep) != -1) {




         //   System.out.println("Timestep is " + timeStep);

            long now = Clock.systemDefaultZone().millis();
            System.out.println(now);

            long diff = now - previousTime;     //actual time step

            //Computes a set of actions from the graph

            System.out.println("Center of mass is: " + graph.centerOfMass.dispCoords());

            Opt gen = new Opt(new CPGNeural[]{cpgNeural1, cpgNeural3, cpgNeural5, cpgNeural7});
            CPGNeural[] r = gen.iter();
            cpgNeural1 = r[0];
            cpgNeural3 = r[1];
            cpgNeural5 = r[2];
            cpgNeural7 = r[3];



            counter++;

            if(counter % 100 == 0) {
                double position = Math.PI / 2;
                for(int i =0; i<num_robots;i++) {

                    int leg_h = leg_counter % 4;

                    if(leg_h == 0 && i==0){
                        cpgNeural1.step();
                        double[] motorPos = cpgNeural1.getMotorPos();
                        graph.rotateX(cpgNeural1.changerateAngle());
                        communicators[i].emit(motorPos[0]);
                    } else if(leg_h == 1 && i==2){
                        cpgNeural1.step();
                        double[] motorPos = cpgNeural3.getMotorPos();
                        graph.rotateX(cpgNeural3.changerateAngle());
                        communicators[i].emit(motorPos[0]);
                    } else if(leg_h == 2 && i==4){
                        cpgNeural1.step();
                        double[] motorPos = cpgNeural5.getMotorPos();
                        graph.rotateX(cpgNeural5.changerateAngle());
                        communicators[i].emit(motorPos[0]);
                    } else if(leg_h == 3 && i==6){
                        cpgNeural1.step();
                        double[] motorPos = cpgNeural7.getMotorPos();
                        graph.rotateX(cpgNeural7.changerateAngle());
                        communicators[i].emit(motorPos[0]);
                    } else {
                        communicators[i].emit(position);
                    }
                    leg_counter++;

                /*    if(i==0 || i==2 || i==4 || i==6){
                        if(i==0){
                            cpgNeural1.step();
                            double[] motorPos = cpgNeural1.getMotorPos();
                            communicators[i].emit(motorPos[0]);
                        } else if(i==2){
                            cpgNeural3.step();
                            double[] motorPos = cpgNeural3.getMotorPos();
                            communicators[i].emit(motorPos[0]);
                        }else if(i==4){
                            cpgNeural5.step();
                            double[] motorPos = cpgNeural5.getMotorPos();
                            communicators[i].emit(motorPos[0]);
                        } else if(i==3){

                        }
                        else {
                            cpgNeural7.step();
                            double[] motorPos = cpgNeural7.getMotorPos();
                            communicators[i].emit(motorPos[0]);
                        }
                    } else {
                        communicators[i].emit(position);
                    }*/

                }
            }
            for(int i =0; i<num_robots;i++) {
                if (communicators[i].hasNext()) {
                    IMUReading reading = communicators[i].receive();
                    readings[i].add(reading);
                //    System.out.println("Robot"+i+": " + reading);
//                Vector rotationVector = reading.getLinear().rotationVector(yAxisVector);
//                System.out.println("Rotation Vector: " + rotationVector);
//                System.out.println("Expected vector: " + robot1.getRotation());
//                if(counter > 100 && counter % 20 == 0) robot2.setRotation(rotationVector);
                }

            }
            if (counter == 10000) {
                for(int i=0; i<4;i++) {
                    Serializer.saveToFile(CPGNeural.getCPG().toString(), "../..resources/w" + i + ".ser");
                }
                break;
            }

            graph.think();
            //kinematics.sendValues(

        }
        /*
        for(int i =0; i<num_robots;i++) {
            Serializer.saveToFile(readings[i], "../../resources/reading"+i+".ser");
        }
        */
    }

}
