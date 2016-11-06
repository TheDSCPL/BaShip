package server;

import sharedlib.coms.*;
import sharedlib.coms.Package;

public class ClientHandler implements Connection.Handler {

    @Override
    public Package handle(Package object) {
        Package response = new Package();
        response.question = object;
        
        if (object.contents.containsKey("query")) {            
            switch ((String) object.contents.get("query")) {
                case "usernameavailable":
                    response.contents.put("isavaliable", true);
                    break;
            }

        }

        return response;
    }

}
