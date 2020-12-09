import Webots.IMUReadings;
import Utility.*;
import Webots.IMUSensor.IMUReading;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.*;

import java.util.Arrays;
public class SupervisorController2 {

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
        int num_robots= 7;
        ObjectCommunicator<Double, IMUReading>[] communicators = new ObjectCommunicator[num_robots];
        WebotsNode[] robots = new WebotsNode[num_robots];
        IMUReadings[] readings = new IMUReadings[num_robots];

        boolean more = true;

        while(more){
            try{
                num_robots++;
                robots[num_robots-1] = new WebotsNode(supervisor.getFromDef("robot"+num_robots));
            } catch(Exception e){
                num_robots--;
                more =!more;
            }
        }

        for(int i = 1; i<=num_robots; i++){
            communicators[i-1] = new ObjectCommunicator<>(
                    supervisor.getEmitter("emitter"+i), supervisor.getReceiver("receiver"+i), timeStep
            );
            readings[i-1] = new IMUReadings();
        }


        Vector yAxisVector = new Vector(0, 1, 0);

        int counter = 0;
        while (supervisor.step(timeStep) != -1) {

            counter++;

            if(counter % 100 == 0) {
                double position = Math.PI / 2;
                for(int i =0; i<num_robots;i++) {
                    communicators[i].emit(position);
                }
            }
            for(int i =0; i<num_robots;i++) {
                if (communicators[i].hasNext()) {
                    IMUReading reading = communicators[i].receive();
                    readings[i].add(reading);
                    System.out.println("Robot"+i+": " + reading);
//                Vector rotationVector = reading.getLinear().rotationVector(yAxisVector);
//                System.out.println("Rotation Vector: " + rotationVector);
//                System.out.println("Expected vector: " + robot1.getRotation());
//                if(counter > 100 && counter % 20 == 0) robot2.setRotation(rotationVector);
                }

            }
            if (counter == 1000) break;

        }
        /*
        for(int i =0; i<num_robots;i++) {
            Serializer.saveToFile(readings[i], "../../resources/reading"+i+".ser");
        }
        */
    }

}
