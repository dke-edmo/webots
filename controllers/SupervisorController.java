import Webots.IMUReadings;
import Utility.*;
import Webots.IMUSensor.IMUReading;
import Webots.ObjectCommunicator;
import com.cyberbotics.webots.controller.*;

import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class SupervisorController {

    public static void main(String[] args) {

        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        ObjectCommunicator<Integer, IMUReading> communicator1 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter1"), supervisor.getReceiver("receiver1"), timeStep
        );

        ObjectCommunicator<Integer, IMUReading> communicator2 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter2"), supervisor.getReceiver("receiver2"), timeStep
        );

        Node robot1 = supervisor.getFromDef("robot1");
        Node robot2 = supervisor.getFromDef("robot2");

        IMUReadings readings1 = new IMUReadings();
        IMUReadings readings2 = new IMUReadings();

        System.out.println(Arrays.toString(robot1.getField("rotation").getSFRotation()));

        int counter = 0;
        while (supervisor.step(timeStep) != -1) {

            System.out.println(Arrays.toString(robot1.getField("rotation").getSFRotation()));

            counter++;

            if(counter > 100 && counter % 50 == 0) {
                int position = counter % 100 == 0 ? 0 : 1;
                communicator1.emit(position);
                communicator2.emit(position);
            }

            if(communicator1.hasNext()) {
                IMUReading reading = communicator1.receive();
                readings1.add(reading);
                System.out.println("Robot1: " + reading);
                System.out.println(Arrays.toString(robot1.getField("rotation").getSFRotation()));
                System.out.println(reading.getLinear().unitVector());
            }

            if(communicator2.hasNext()) {
                IMUReading reading = communicator2.receive();
                readings2.add(reading);
                System.out.println("Robot2: " + reading);
                System.out.println(Arrays.toString(robot2.getField("rotation").getSFRotation()));
                System.out.println(reading.getLinear().unitVector());
            }

            if(readings1.getAll().size() != 0 && readings2.getAll().size() != 0) {
                Vector vector1 = readings1.toVector();
                Vector vector2 = readings2.toVector();
                System.out.println(
                    "Readings euclidean distance: " + Utils.round(vector1.euclideanDistance(vector2)) +
                        " cosine similarity: " + Utils.round(vector1.cosineSimilarity(vector2))
                );

                Vector vector3 = readings1.toVector().logNormalize();
                Vector vector4 = readings2.toVector().logNormalize();
                System.out.println(
                    "Log normalized Readings euclidean distance: " + Utils.round(vector3.euclideanDistance(vector4)) +
                        " cosine similarity: " + Utils.round(vector3.cosineSimilarity(vector4))
                );
            }

            if(counter == 1000) break;

        }

        Serializer.saveToFile(readings1, "./resources/reading1.ser");
        Serializer.saveToFile(readings2, "./resources/reading2.ser");

    }

}
