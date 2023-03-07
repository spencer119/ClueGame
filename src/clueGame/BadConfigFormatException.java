package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Exception class for config files with bad formatting
 */
public class BadConfigFormatException extends Exception {
    public BadConfigFormatException() {
        super("Bad config file format.");
        try {
            PrintWriter log = new PrintWriter("log.txt");
            log.println("Bad config file format.");
            log.close();

        } catch (FileNotFoundException e) {
            System.out.println("Failed to write to log file.");
        }
    }

//    public BadConfigFormatException(String message) {
//        super(message);
//    }
}
