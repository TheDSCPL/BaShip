package server.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread responsible for handling the console input from the administrator and
 * running the various commands.
 *
 * @author Alex
 */
public class Console extends Thread {

    private static final String PROMPT = "> ";
    
    public void println(String output) {
        System.out.print("\b\b" + output + "\n" + PROMPT);
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(PROMPT);
            
            String in = null;
            try {
                in = reader.readLine();
            }
            catch (IOException ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (in != null && !in.equals("\n") && !in.isEmpty()) {
                System.out.println(runCommand(in));
            }
        }
    }
    
    private String runCommand(String command) {
        return "Console not yet operational"; // TODO: Luis: implementa isto
    }
}
