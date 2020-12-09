import Webots.IMUReadings;
import Utility.*;
import Webots.IMUSensor.IMUReading;
import Webots.ObjectCommunicator;
import Webots.WebotsNode;
import com.cyberbotics.webots.controller.*;

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
        ObjectCommunicator<Double, IMUReading> communicator3 = new ObjectCommunicator<>(
                supervisor.getEmitter("emitter3"), supervisor.getReceiver("receiver3"), timeStep
        );
        ObjectCommunicator<Double, IMUReading> communicator4 = new ObjectCommunicator<>(
                supervisor.getEmitter("emitter4"), supervisor.getReceiver("receiver4"), timeStep
        );
        ObjectCommunicator<Double, IMUReading> communicator5 = new ObjectCommunicator<>(
                supervisor.getEmitter("emitter5"), supervisor.getReceiver("receiver5"), timeStep
        );
        ObjectCommunicator<Double, IMUReading> communicator6 = new ObjectCommunicator<>(
                supervisor.getEmitter("emitter6"), supervisor.getReceiver("receiver6"), timeStep
        );
        ObjectCommunicator<Double, IMUReading> communicator7 = new ObjectCommunicator<>(
                supervisor.getEmitter("emitter7"), supervisor.getReceiver("receiver7"), timeStep
        );

        WebotsNode robot1 = new WebotsNode(supervisor.getFromDef("robot1"));
        WebotsNode robot2 = new WebotsNode(supervisor.getFromDef("robot2"));
        WebotsNode robot3 = new WebotsNode(supervisor.getFromDef("robot3"));
        WebotsNode robot4 = new WebotsNode(supervisor.getFromDef("robot4"));
        WebotsNode robot5 = new WebotsNode(supervisor.getFromDef("robot5"));
        WebotsNode robot6 = new WebotsNode(supervisor.getFromDef("robot6"));
        WebotsNode robot7 = new WebotsNode(supervisor.getFromDef("robot7"));


        IMUReadings readings1 = new IMUReadings();
        IMUReadings readings2 = new IMUReadings();
        IMUReadings readings3 = new IMUReadings();
        IMUReadings readings4 = new IMUReadings();
        IMUReadings readings5 = new IMUReadings();
        IMUReadings readings6 = new IMUReadings();
        IMUReadings readings7 = new IMUReadings();

        Vector yAxisVector = new Vector(0, 1, 0);

        int counter = 0;
        while (supervisor.step(timeStep) != -1) {

            counter++;

            if(counter % 100 == 0) {
                double position = Math.PI / 2;
                communicator1.emit(position);
                communicator2.emit(position);
                communicator3.emit(position);
                communicator4.emit(position);
                communicator5.emit(position);
                communicator6.emit(position);
                communicator7.emit(position);
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

            if(communicator3.hasNext()) {
                IMUReading reading = communicator3.receive();
                readings3.add(reading);
                System.out.println("Robot3: " + reading);
            }
            if(communicator4.hasNext()) {
                IMUReading reading = communicator4.receive();
                readings4.add(reading);
                System.out.println("Robot4: " + reading);
            }
            if(communicator5.hasNext()) {
                IMUReading reading = communicator5.receive();
                readings5.add(reading);
                System.out.println("Robot5: " + reading);
            }
            if(communicator6.hasNext()) {
                IMUReading reading = communicator6.receive();
                readings6.add(reading);
                System.out.println("Robot6: " + reading);
            }
            if(communicator7.hasNext()) {
                IMUReading reading = communicator7.receive();
                readings7.add(reading);
                System.out.println("Robot7: " + reading);
            }

            if(counter == 1000) break;

        }

        Serializer.saveToFile(readings1, "../../resources/reading1.ser");
        Serializer.saveToFile(readings2, "../../resources/reading2.ser");
        Serializer.saveToFile(readings3, "../../resources/reading3.ser");
        Serializer.saveToFile(readings4, "../../resources/reading4.ser");
        Serializer.saveToFile(readings5, "../../resources/reading5.ser");
        Serializer.saveToFile(readings6, "../../resources/reading6.ser");
        Serializer.saveToFile(readings7, "../../resources/reading7.ser");

    }

}
