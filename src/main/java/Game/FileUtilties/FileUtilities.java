package Game.FileUtilties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtilities {
    /**
     * Helpful method to avoid repeating boilerplate code for writing text to a file.
     * @param stringPath Path to file location.
     * @param content String containing the content to be put in the file.
     * @param cs The character set to be used for the file.
     * @return the path of the written file as a string.
     * TODO: This method needs better error handling
     */
    public static String writeText(String stringPath, String content, Charset cs) {
        List<String> lines = Arrays.asList(content.split("\n"));
        Path path = Paths.get(stringPath);
        try {
            if (ensureFileExists(stringPath)) {
                Files.write(path, lines, cs);
            } else {
                System.out.println(stringPath + " was not successfully written to.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.toString();
    }

    /**
     * Helpful overloaded method of writeText(String, String, Charset). This
     * method uses the UTF-8 charset by default.
     * @param stringPath Path to file location.
     * @param content String containing the content to be put in the file.
     * @return the path of the written file as a string.
     */
    public static String writeText(String stringPath, String content) {
        return writeText(stringPath, content, Charset.forName("UTF-8"));
    }

    /**
     * Helpful method that takes a string path, turns it into a File object,
     * and then returns whatever .toString() gives when called on the File
     * object but the string has also has concatenated a \ at the end. This is
     * helpful because it lets File take care of the issue of whether there is
     * a \ at the end. File gives a string without that slash at the end.
     * @param path The path to be returned with one slash at the end.
     * @return The parameter, path, with a slash at the end.
     */
    public static String getDirectoryWithSlash(String path) {
        return new File(path).toString() + "\\";
    }

    /**
     * This method can be used to ensure that directories a path points through are created, if necessary, along with
     * the file the path points to.
     *
     * @param stringPath The string path which points to a file. Can be relative or absolute.
     * @return Returns a boolean indicating whether the directories and the file exist.
     */
    public static boolean ensureFileExists(String stringPath) throws IOException {
        File file = new File(stringPath);
        File parentFile = file.getParentFile();
        parentFile.mkdirs();
        file.createNewFile();
        return parentFile.exists() && file.exists();
    }
}
