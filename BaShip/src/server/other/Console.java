package server.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import server.database.UserDB;
import server.logic.UserS;
import sharedlib.constants.DBK;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;
import sharedlib.utils.Crypto;
import static sharedlib.utils.CredentialsChecker.*;
import server.conn.Server;

/**
 * Thread responsible for handling the console input from the administrator and
 * running the various commands.
 *
 * @author Luis
 */
public class Console extends Thread {

    private static final String PROMPT = "> ";
    private static final Map<String, Command> COMMANDS;
    private static final String LINE = System.lineSeparator();

    private volatile static boolean SQLExceptionOccurred = false;
    
    static {
        COMMANDS = new HashMap<>();
        Command users = new Command("users", null) {
            @Override
            public String run(List<String> args) {
                if (args.size() <= 0) {
                    return helpMenu();
                }
                Command com = getSubCommand(args.get(0));
                if (com != null) {
                    args.remove(0);
                    return com.run(args);
                }
                return invalidUsage();
            }

            @Override
            public String helpMenu(int startAt) {
                return "\"" + name + "\"" + " usage:" + LINE
                        + super.helpMenu(startAt);
            }
        };
            Command usersAdd = new Command("add", users) {
                @Override
                public String run(List<String> args) {
                    if (args.size() != 2) {
                        return "You must indicate the username and password. Use double quotes for space-containing fileds.";
                    }
                    UserInfo ui;
                    try {
                        if (!isUsernameValid(args.get(0))) {
                            throw new UserMessageException("Invalid username");
                        }
                        if (!isPasswordValid(args.get(1).toCharArray())) {
                            throw new UserMessageException("Invalid password");
                        }
                        ui = UserS.register(null, args.get(0), Crypto.SHA1(args.get(1).toCharArray()));
                    } catch (UserMessageException ex) {
                        return ex.getMessage();
                    }
                    return "User \"" + ui.username + "\" successfully added.";
                }

                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"add USERNAME PASSWORD\" - to add a new user"
                            + super.helpMenu(startAt);
                }

            };
            Command usersList = new Command("list", users) {
                @Override
                public String run(List<String> args) {
                    String filter = "";
                    int pageIndex = 0;
                    int pageSize = DBK.pageSize;

                    int temp;
                    if ((temp = args.indexOf("-f")) != -1) {
                        if (temp + 1 >= args.size()) {
                            return "Invalid usage of option \"-f\"!" + LINE
                                    + helpMenu();
                        }
                        filter = args.get(temp + 1);
                    }
                    if ((temp = args.indexOf("-p")) != -1) {
                        if (temp + 1 >= args.size()) {
                            return "Invalid usage of option \"-p\"!" + LINE
                                    + helpMenu();
                        }
                        try {
                            pageIndex = Integer.parseInt(args.get(temp + 1));
                            if (pageIndex < 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            return "Invalid page index!";
                        }
                    }
                    if ((temp = args.indexOf("-n")) != -1) {
                        if (temp + 1 >= args.size()) {
                            return "Invalid usage of option \"-n\"!" + LINE
                                    + helpMenu();
                        }
                        try {
                            pageSize = Integer.parseInt(args.get(temp + 1));
                            if (pageSize < 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            return "Invalid page size!";
                        }
                    }

                    List<UserInfo> ui;
                    try {
                        ui = UserDB.getUserList(new UserSearch(filter, pageIndex), pageSize);
                    } catch (SQLException ex) {
                        return ex.getMessage();
                    }

                    if (ui == null || ui.size() <= 0) {
                        return "No users found";
                    }

                    return "Found " + ui.size() + " users:" + ui.stream().map(e -> e.username).reduce("", (a, b) -> a + LINE + "-\"" + b + "\"");
                }

                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"list\" [-f \"FILTER\"] [-p PAGE_INDEX] [-n USERS_PER_PAGE]" + LINE
                            + tabs(startAt - 1) + "OPTIONS:" + LINE
                            + tabs(startAt - 1) + "\"-f FILTER\" -> to filter the list. Use double quotes for including spaces." + LINE
                            + tabs(startAt - 1) + "\"-p PAGE_INDEX\" -> the index of the page (default is 0)" + LINE
                            + tabs(startAt - 1) + "\"-n USERS_PER_PAGE\" -> number of users per page (default is " + DBK.pageSize + ')'
                            + super.helpMenu(startAt);
                }
            };
            Command usersBan = new Command("ban", users) {
                @Override
                public String run(List<String> args) {

                    if (args.size() <= 0) {
                        return "Invalid usage of \"" + name + "\"." + LINE
                                + helpMenu();
                    }
                    StringBuilder ret = new StringBuilder();
                    //Usernames that don't exist
                    args.stream().filter(u -> throwlessIsUserAvailable(u)).forEach(u -> ret.append("User \"").append(u).append("\" doesn't exist. Ignoring...").append(LINE));
                    //Users that are alreay banned
                    args.stream().filter(u -> !throwlessIsUserAvailable(u) && isBanned(u)).forEach(u -> ret.append("User \"").append(u).append("\" is already banned. Ignoring...").append(LINE));                    
                    //Ban valid and existing users
                    args.stream().filter(u -> isUsernameValid(u) && !throwlessIsUserAvailable(u) && !isBanned(u)).forEach(u -> {banUser(u); ret.append("User \"").append(u).append("\" banned.").append(LINE);});

                    if(SQLExceptionOccurred) {
                        ret.append("An SQL error occurred!");
                    }
                    
                    return ret.toString();
                }
                @Override
                public String helpMenu(int startAt)
                {
                    return tabs(startAt) + "\"" + name + " USERNAMES...\" -> bans all of the inerted usernames";
                }
            };
            Command usersUnBan = new Command("unban", users) {
                @Override
                public String run(List<String> args) {

                    if (args.size() <= 0) {
                        return "Invalid usage of \"" + name + "\"" + LINE
                                + helpMenu();
                    }
                    StringBuilder ret = new StringBuilder();
                    //Usernames that don't exist
                    args.stream().filter(u -> throwlessIsUserAvailable(u)).forEach(u -> ret.append("User \"").append(u).append("\" doesn't exist. Ignoring...").append(LINE));
                    //Users that are alreay banned
                    args.stream().filter(u -> !throwlessIsUserAvailable(u) && !isBanned(u)).forEach(u -> ret.append("User \"").append(u).append("\" is already unbanned. Ignoring...").append(LINE));
                    //Ban valid and existing users
                    args.stream().filter(u -> isUsernameValid(u) && !throwlessIsUserAvailable(u) && isBanned(u)).forEach(u -> {unbanUser(u); ret.append("User \"").append(u).append("\" unbanned.").append(LINE);});

                    if(SQLExceptionOccurred) {
                        ret.append("An SQL error occurred!");
                    }
                    
                    return ret.toString();
                }
                @Override
                public String helpMenu(int startAt)
                {
                    return tabs(startAt) + "\"" + name + " USERNAMES...\" -> unbans all of the inerted usernames";
                }
            };
            Command usersIsBanned = new Command ("isBanned", users) {
            @Override
                public String run(List<String> args) {
                    if(args.size() <= 0)
                        return "Invalid usage of \"" + name + "\"." + LINE
                             + helpMenu();
                    StringBuilder ret = new StringBuilder();
                    //Usernames that don't exist
                    args.stream().filter(u -> throwlessIsUserAvailable(u)).forEach(u -> ret.append("User \"").append(u).append("\" doesn't exist. Ignoring...").append(LINE));
                    //Users that are alreay banned
                    args.stream().filter(u -> !throwlessIsUserAvailable(u)).forEach(u -> ret.append("User \"").append(u).append("\": ").append(isBanned(u)?"baned":"unbanned").append(LINE));  
                    
                    return ret.toString();
                }
                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"" + name + " USERNAMES...\" -> shows to all of those (existing) usernames wether they are or aren't banned";
                }
            };
        users.addCommands(usersAdd, usersList,usersBan,usersUnBan,usersIsBanned);
        COMMANDS.put(users.name, users);

        Command server = new Command("server", null) {
            @Override
            public String run(List<String> args) {
                if (args.size() <= 0) {
                    return helpMenu();
                }
                Command com = getSubCommand(args.get(0));
                if (com != null) {
                    args.remove(0);
                    return com.run(args);
                }
                return invalidUsage();
            }

            @Override
            public String helpMenu(int startAt) {
                return "\"" + name + "\"" + " usage:" + LINE
                     + super.helpMenu(startAt);
            }
        };
            Command serverStatus = new Command("status", server) {
                @Override
                public String run(List<String> args) {
                    return "Server status: " + (Server.isRunning() ? "Running" : "Stopped");
                }

                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"" + name + "\" -> shows wether the server is running or not";
                }
            };
            Command serverStart = new Command("start", server) {
                @Override
                public String run(List<String> args) {
                    try {
                        Server.startServer();
                    } catch (IOException ex) {
                        Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                        return "Error starting server";
                    }

                    return "Server started";
                }

                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"" + name + "\" -> Starts the server. If it was already running, restarts it.";
                }
            };
            Command serverStop = new Command("stop", server) {
                @Override
                public String run(List<String> args) {
                    if (!Server.isRunning()) {
                        return "A wild server has appeared!" + LINE
                                + "You chose to throw \"stop\" at it." + LINE
                                + "It's not very effective...";
                    }
                    try {
                        Server.stopServer();
                    } catch (IOException ex) {
                        Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                        return "Error stopping server";
                    }

                    return "Server stopped";
                }

                @Override
                public String helpMenu(int startAt) {
                    return tabs(startAt) + "\"" + name + "\" -> Stops the server. If it was already stopped this command will have no effect.";
                }
            };
        server.addCommands(serverStart, serverStop, serverStatus);
        COMMANDS.put(server.name, server);

        Command exit = new Command("exit", null) {
            @Override
            public String run(List<String> args) {
                System.exit(0);
                return "";
            }

            @Override
            public String helpMenu(int startAt) {
                return "\"" + name + "\" -> Terminates the execution of the server."
                        + super.helpMenu(startAt);
            }
        };
        COMMANDS.put(exit.name, exit);

        Command help = new Command("help", null) {
            @Override
            public String run(List<String> args) {
                if (args.size() <= 0) {
                    return "Command list:" + COMMANDS.keySet().stream().filter(k -> !k.equals("help")).reduce("", (a, b) -> a + LINE + b) + LINE + helpMenu();
                }
                return args.stream().map(arg -> COMMANDS.get(arg)).filter(o -> o != null).map(com -> com.helpMenu(0)).reduce("", (a, b) -> a + LINE + b);
            }

            @Override
            public String helpMenu(int startAt) {
                return tabs(startAt) + "help [COMMAND...] -> to get the help menu." + LINE
                        + tabs(startAt - 1) + "COMMAND... -> optional parameters used to get the help menu of those commands. Unknown commands will be ignored.";
            }
        };
        COMMANDS.put(help.name, help);
    }

    private static void banUser(String username)
    {
        try {
            UserDB.setUserBanned(username, true);
        } catch (SQLException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            SQLExceptionOccurred = true;
        }
    }
    
    private static void unbanUser(String username)
    {
        try {
            UserDB.setUserBanned(username, false);
        } catch (SQLException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            SQLExceptionOccurred = true;
        }
    }
    
    private static boolean isBanned(String username)
    {
        try {
            return UserDB.isUserBanned(username);
        } catch (SQLException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            SQLExceptionOccurred = true;
        }
        return true;
    }
    
    private static boolean throwlessIsUserAvailable(String username)
    {
        try {
            return UserDB.isUsernameAvailable(username);
        } catch (SQLException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            SQLExceptionOccurred = true;
            return false;
        }
    }
    
    /**
     * Prints a message.
     * @param output The message to be printed
     */
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
            } catch (IOException ex) {
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
        if (com == null) {
            return "Command \"" + tokens.get(0) + "\" is invalid." + LINE + COMMANDS.get("help").run(new ArrayList<String>());
        }
        tokens.remove(0);
        return com.run(tokens);
    }

    private List<String> tokenize(String input) {
        java.util.ArrayList<String> output = new java.util.ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);  //spaces or blocks of double quotes
        while (m.find()) {
            output.add(m.group(1).replace("\"", ""));
        }
        return output;
    }

    private static abstract class Command {

        Command(String name, Command parent, Command... subCommands) {
            this.name = name;

            this.parent = parent;

            addCommands(subCommands);
        }
        public final String name;
        protected final Command parent;
        private Map<String, Command> subCommands;

        public abstract String run(List<String> args);

        public final String helpMenu() {
            return helpMenu(getDepth());
        }

        /**
         * Returns the help menu of this command/subcommand and all of its
         * subcommands.<br>
         * <u>Should <b>NOT</b> have a new line at the end</u>
         *
         * @param startAt the depth of the command
         * @return
         */
        public String helpMenu(int startAt) {
            StringBuilder ret = new StringBuilder("");
            boolean first = true;
            for (Command c : subCommands.values()) {
                ret.append(first ? "" : LINE).append(c.helpMenu(startAt)).append(LINE);
            }
            return ret.toString();
        }

        /*protected final String unknownOption(String option)
        {
            return "Unknown option \"" + option + "\"." + LINE + helpMenu();
        }*/
        protected final String invalidUsage() {
            return "Invalid usage of \"" + name + "\"!" + LINE + helpMenu();
        }

        protected final int getDepth() {
            if (parent == null) {
                return 0;
            }
            return parent.getDepth() + 1;
        }

        protected final String tabs(int startAt) {
            StringBuilder ret = new StringBuilder("");
            int depth = getDepth() - startAt;
            for (int i = 0; i < depth; i++) {
                ret.append("  ");
            }
            return ret.toString();
        }

        protected final void addCommands(Command... subCommands) {
            Map<String, Command> temp;
            if (this.subCommands == null) {
                temp = new HashMap<>();
            } else {
                temp = this.subCommands;
            }

            java.util.Arrays.asList(subCommands).stream().forEach(v -> temp.put(v.name, v));

            this.subCommands = temp;
        }

        protected final Map<String, Command> getSubCommands() {
            return subCommands;
        }

        protected final Command getSubCommand(String sc) {
            return this.subCommands.get(sc);
        }
    }
}
