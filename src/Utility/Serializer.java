package Utility;

import java.io.*;

/**
 * Provides a way to serialize object to file system.
 *
 * @author Tomasz Darmetko
 */
public class Serializer {

    public static void saveToFile(Object object, String filepath) {
        try(FileOutputStream fileStream = new FileOutputStream(filepath)) {
            try(ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
                objectStream.writeObject(object);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object readFromFile(String filepath) {
        try(FileInputStream fileStream = new FileInputStream(filepath)) {
            try(ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
                return objectStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
