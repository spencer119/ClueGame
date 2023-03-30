package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Exception class for config files with bad formatting in the setup process
 */
public class BadConfigFormatException extends Exception {
    public BadConfigFormatException() {
        super("Bad config file format.");
    }

    public BadConfigFormatException(String message) {
        super(message);
        try { // If a custom message is passed attempt to write to a log file
            PrintWriter log = new PrintWriter("log.txt");
            log.println(message);
            log.close();

        } catch (FileNotFoundException e) { // Catch file errors
            System.out.println("Failed to write to log file.");
        }
    }
}
