package server.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import server.conn.Server;

/**
 * Thread responsible for handling the console input from the administrator and
 * running the various commands.
 *
 * @author Alex
 */
public class Console extends Thread {

    private static final String PROMPT = "> ";
    private static final Map<String,Command> COMMANDS;
    
    static {
        COMMANDS = new HashMap<>();
        COMMANDS.put("help", new Command() {
            @Override
            public String run(List<String> args)
            {
                return "help";
            }
        });
        COMMANDS.put("start", new Command() {
            @Override
            public String run(List<String> args)
            {
                try {
                    Server.startServer();
                }
                catch (IOException ex) {
                    Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                    return "Error starting server";
                }
                
                return "";
            }
        });
        COMMANDS.put("stop", new Command() {
            @Override
            public String run(List<String> args)
            {
                try {
                    Server.stopServer();
                }
                catch (IOException ex) {
                    Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                    return "Error stopping server";
                }
                
                return "";
            }
        });
        COMMANDS.put("exit", new Command() {
            @Override
            public String run(List<String> args)
            {
                System.exit(0);
                return "";
            }
        });
    }
    
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
        List<String> tokens = tokenize(command);
        Command com = COMMANDS.get(tokens.get(0));
        if(com == null)
            return "Command \"" + tokens.get(0) + "\" is invalid.";
        tokens.remove(0);
        return com.run(tokens);
    }
    
    private List<String> tokenize(String input)
    {
        java.util.ArrayList<String> output = new java.util.ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);  //spaces or blocks of double quotes
        while(m.find())
            output.add(m.group(1).replace("\"", ""));
        return output;
    }
    
    private static abstract class Command {
        Command(Class<?> ... argTypes) {
            List<Class<?>> temp = new ArrayList<>();
            for(Class<?> c : argTypes)
                temp.add(c);
            this.argTypes = Collections.unmodifiableList(temp);
        }
        public abstract String run(List<String> args);
        public final List<Class<?>> argTypes;
    }
}
