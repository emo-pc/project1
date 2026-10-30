import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** Entry point. The two command-line arguments are the input and output paths. */
public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            java.lang.System.err.println("Usage: java Main <input file> <output file>");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
            String line;
            OS realEstateSystem = new OS();
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Your executer
                    realEstateSystem.commandExecuter(line,writer);
                }
            }
        } catch (IOException exception) {
            java.lang.System.err.println("File operation failed: " + exception.getMessage());
        }
    }
}
