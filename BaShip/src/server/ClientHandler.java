package server;

import sharedlib.coms.*;

/**
 *
 * @author Alex
 */
public class ClientHandler implements Connection.Handler {

    @Override
    public ConnectionObject handle(ConnectionObject object) {
        if (object.contents.containsKey("query")) {

            switch ((String) object.contents.get("query")) {
                case "usernameavailable":
                    object.contents.put("isavaliable", true);
                    break;
            }

        }

        return object;
    }

}
