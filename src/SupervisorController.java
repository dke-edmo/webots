import Webots.IMUReadings;
import Utility.*;
import Webots.IMUSensor.IMUReading;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class SupervisorController {

    public static void main(String[] args) {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Supervisor supervisor = new Supervisor();
        int timeStep =  (int)Math.round(supervisor.getBasicTimeStep());

        // (Merijn) testing...

        //rootChildrenField.importMFNode(-1, "../../resources/EDMO.wbo");
        /*
        File bot = new File("../../resources/EDMO.wbo");
        String botString = bot.toString().replace("_id_", "3");
        System.out.println(botString);*/
        Node root = supervisor.getRoot();
        Field rootChildrenField = root.getField("children");
        byte[] encoded = null;
        try
        {
            encoded = Files.readAllBytes(Paths.get("../../resources/EDMO.wbo"));
        }
        catch (IOException e)
        {
            System.out.println("IOException at importing a new edmo module");
        }
        String botString =  new String(encoded, StandardCharsets.US_ASCII);
        botString = botString.replace("__id__", "3");
        rootChildrenField.importMFNodeFromString(-1,botString);
        Node node = rootChildrenField.getMFNode(-1);
        Field field = node.getField("translation");
        double[] location = {-1d, 0.5d, 0d};
        field.setSFVec3f(location);


        ObjectCommunicator<Double, IMUReading> communicator1 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter1"), supervisor.getReceiver("receiver1"), timeStep
        );

        ObjectCommunicator<Double, IMUReading> communicator2 = new ObjectCommunicator<>(
            supervisor.getEmitter("emitter2"), supervisor.getReceiver("receiver2"), timeStep
        );

        WebotsNode robot1 = new WebotsNode(supervisor.getFromDef("robot1"));
        WebotsNode robot2 = new WebotsNode(supervisor.getFromDef("robot2"));

        IMUReadings readings1 = new IMUReadings();
        IMUReadings readings2 = new IMUReadings();

        Vector yAxisVector = new Vector(0, 1, 0);

        int counter = 0;
        while (supervisor.step(timeStep) != -1) {

            counter++;

            if(counter <= 100 && counter % 50 == 0) {
                double position = counter % 100 == 0 ? 0 : 1;
                communicator1.emit(position);
                communicator2.emit(position);
            }

            if(communicator1.hasNext()) {
                IMUReading reading = communicator1.receive();
                readings1.add(reading);
                System.out.println("Robot1: " + reading);

                Vector rotationVector = reading.getLinear().rotationVector(yAxisVector);
                System.out.println("Rotation Vector: " + rotationVector);
                System.out.println("Expected vector: " + robot1.getRotation());
                if(counter > 100 && counter % 20 == 0) robot2.setRotation(rotationVector);
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

}
