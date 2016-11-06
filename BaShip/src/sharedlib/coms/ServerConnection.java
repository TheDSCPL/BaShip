package sharedlib.coms;

import java.io.IOException;
import java.net.*;

public class ServerConnection extends Connection {

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
    }

    public boolean usernameAvailable(String username) throws IOException, ClassNotFoundException, InterruptedException {
        Package query = new Package();
        query.contents.put("query", "usernameavailable");
        query.contents.put("username", username);
        return (Boolean) sendAndReceive(query).contents.get("isavaliable");
    }

    /**
     * Checks if a combination of username/password is valid or not
     *
     * @param username
     * @param password
     * @return
     */
    public boolean checkUsernamePasswordCombination(String username, String password) {
        return false;
    }
}
