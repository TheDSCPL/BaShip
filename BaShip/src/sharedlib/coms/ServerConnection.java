package sharedlib.coms;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author Alex
 */
public class ServerConnection extends Connection {

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public synchronized void start() {
        /*synchronized (ServerMain.clients) {
            ServerMain.clients.add(this);
        }*/
        
        super.start();
        
        /*synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }*/
    }
    
    /*public void process() throws IOException, ClassNotFoundException {
        ConnectionObject object = receive();
        
        if (object.contents.containsKey("query")) {
            switch ((String)object.contents.get("query")) {
                case "usernameavailable":
                    object.contents.put("isavaliable", true);
                    break;
            }
        }
        
        send(object);
    }*/
}
