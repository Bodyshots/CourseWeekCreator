package src;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;

public class PathFinder {
    
    public static String getDefault() {
        return Paths.get("").toAbsolutePath().toString();
    }

    public static String getRelative(String filePath) { // may implement in the future
        try {
            String file = new File(filePath).getCanonicalPath();
            return file;
        }
        catch(IOException e) {
            System.out.println("Error: I/O Exception occurred");
            return null;
        }
    }

}
