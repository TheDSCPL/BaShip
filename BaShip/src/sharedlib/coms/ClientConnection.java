package sharedlib.coms;

import java.io.IOException;
import java.net.*;

public class ClientConnection extends Connection {

    
    
    public ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    public boolean usernameAvailable(String username) throws IOException, ClassNotFoundException, InterruptedException {
        ConnectionObject query = new ConnectionObject();
        query.contents.put("query", "usernameavailable");
        query.contents.put("username", username);        
        return (Boolean) sendAndReceive(query).contents.get("isavaliable");
    }

}
