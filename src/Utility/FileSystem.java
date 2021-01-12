package Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {

    public static final String workingDirectory = System.getProperty("user.dir");
    public static final String projectRootDirectory = workingDirectory + "/../../";
    public static final String webotsDirectory = projectRootDirectory + "/webots/";
    public static String readString(String filepath) {
        /*
        try {
            return Files.readString(Path.of(filepath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }*/
        return null;
    }

}
